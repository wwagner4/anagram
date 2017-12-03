package anagram.ml.rate

import java.nio.file.Path

import anagram.common.IoUtil
import anagram.ml.MlUtil
import anagram.ml.data.common.{BookCollections, BookSplitterTxt}
import anagram.model.{CfgRaterAi, SentenceLength}
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

class RaterAi(cfg: CfgRaterAi, logInterval: Option[Int] = Some(1000)) extends Rater {

  case class Model(
                    sentenceLength: SentenceLength,
                    multiLayerNetwork: MultiLayerNetwork,
                  )

  private val log = LoggerFactory.getLogger("RaterAi")

  require(logInterval.forall(n => n > 0), "If loginterval is defined it must be greater 0")

  var cnt = 0

  private val nnModel: Map[Int, Model] = cfg.sentenceLengths
    .map(sl => (sl.length, Model(
      sl,
      deserializeNn(IoUtil.dirWork.resolve(MlUtil.nnFileName(cfg.id, sl.length)))
    )))
    .toMap

  def rate(sent: Iterable[String]): Double = {
    val _size = sent.size
    nnModel.get(_size) match {
      case Some(m) => rate(m.multiLayerNetwork, m.sentenceLength, sent)
      case None => if (_size <= 1.0) 100 else -100
    }
  }

  def rate(nn: MultiLayerNetwork, sentenceLength: SentenceLength, sent: Iterable[String]): Double = {
    logInterval.foreach { interv =>
      if (cnt % interv == 0 && cnt > 0) log.info(s"Rated $cnt sentences")
    }
    cnt += 1

    def rateNN = {
      val input: Array[Double] = sent
        .map(cfg.mapper.transform)
        .flatMap(_.map(cfg.mapper.toNum(_).toDouble))
        .toArray
      val out = nn.output(Nd4j.create(input))
      out.getDouble(0)
    }


    val _ratingNN = rateNN

    if (cfg.adjustOutput) _ratingNN + sentenceLength.ratingAdjustOutput
    else _ratingNN


  }


  private def deserializeNn(path: Path): MultiLayerNetwork = {
    ModelSerializer.restoreMultiLayerNetwork(path.toFile)
  }

  def commonWords: Set[String] = {
    val coll = BookCollections.collectionEn2
    val splitter = new BookSplitterTxt
    coll.books
      .map(_.filename)
      .flatMap(resName => splitter.splitSentences(resName))
      .flatten
      .groupBy(identity)
      .toList
      .filter(_._1.length > 3)
      .map { case (w, ws) => (w, ws.size) }
      .sortBy(-_._2)
      .map(_._1)
      .take(2000)
      .toSet
  }

}

