package anagram.solve

import java.nio.file.Path

import anagram.common.IoUtil
import anagram.ml.data.{BookCollections, BookSplitterTxt}
import anagram.words.WordMapper
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.util.ModelSerializer
import org.nd4j.linalg.factory.Nd4j
import org.slf4j.LoggerFactory

import scala.util.Random

trait Rater {
  def rate(sent: Iterable[String]): Double
}

class RaterRandom extends Rater {

  def rate(sent: Iterable[String]): Double = {
    Random.nextDouble() * 10
  }

}

class RaterNone extends Rater {

  def rate(sent: Iterable[String]): Double = {
    1.0
  }

}

class RaterAi(dataId: String, wordmap: WordMapper, logInterval: Option[Int] = Some(1000)) extends Rater {

  private val log = LoggerFactory.getLogger("RaterAi")

  private lazy val _commonWords = commonWords

  require(logInterval.forall(n => n > 0), "If loginterval is defined it must be greater 0")

  var cnt = 0

  private val nnMap: Map[Int, MultiLayerNetwork] = IoUtil.getNnDataFilesFromWorkDir(dataId)
    .map(df => (df.wordLen, deserializeNn(df.path)))
    .toMap

  require(nnMap.nonEmpty, s"Found no NNs for dataId: '$dataId'")

  def rate(sent: Iterable[String]): Double = {
    if (sent.size <= 1) 1.0
    else if (sent.size >= 6) 0.0
    else {
      nnMap.get(sent.size)
        .map(rate(_, sent))
        .getOrElse(throw new IllegalStateException(s"Found no NN for dataId $dataId and sentence size ${sent.size}"))
    }
  }

  def rate(nn: MultiLayerNetwork, sent: Iterable[String]): Double = {
    logInterval.foreach{interv =>
      if (cnt % interv == 0 && cnt > 0) log.info(s"Rated $cnt sentences")
    }

    def rateNN = {
      val input: Array[Double] = sent
        .map(wordmap.group)
        .map(wordmap.toNum(_).toDouble)
        .toArray
      val out = nn.output(Nd4j.create(input))
      out.getDouble(0)
    }


    val _ratingNN = rateNN
    val _ratingCommoWords = CommonWordRater.rateCommonWords(sent, _commonWords, 0.01)

    _ratingNN + _ratingCommoWords
  }


  private def deserializeNn(path: Path): MultiLayerNetwork = {
    ModelSerializer.restoreMultiLayerNetwork(path.toFile)
  }

  def commonWords: Set[String] = {
    val coll = BookCollections.collectionEn2
    val splitter = new BookSplitterTxt
    coll.books
      .map(_.filename)
      .flatMap(resName => splitter.splitSentences(IoUtil.uri(resName)))
      .flatten
      .groupBy(identity)
      .toList
      .filter(_._1.length > 2)
      .map { case (w, ws) => (w, ws.size) }
      .sortBy(-_._2)
      .map(_._1)
      .take(2000)
      .toSet
  }

}

object CommonWordRater {

  def rateCommonWords(sent:Iterable[String], commonWords: Set[String], commonFactor: Double): Double = {
    sent.map(w => if (commonWords.contains(w)) 1 else 0).sum * commonFactor
  }
}