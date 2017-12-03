package anagram.ml.rate

import java.nio.file.Path

import anagram.common.IoUtil
import anagram.ml.MlUtil
import anagram.ml.data.common.{BookCollections, BookSplitterTxt}
import anagram.model.CfgRaterAi
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

  private val log = LoggerFactory.getLogger("RaterAi")

  require(logInterval.forall(n => n > 0), "If loginterval is defined it must be greater 0")

  var cnt = 0

  private val nnMap: Map[Int, MultiLayerNetwork] = cfg.sentenceLengths
    .map(sl => (sl.length, deserializeNn(IoUtil.dirWork.resolve(MlUtil.nnFileName(cfg.id, sl.length)))))
    .toMap

  require(nnMap.nonEmpty, s"Found no NNs for dataId: '${cfg.id}'")

  def rate(sent: Iterable[String]): Double = {
    if (sent.size <= 1) 100
    else if (sent.size >= 6) -100
    else {
      nnMap.get(sent.size)
        .map(rate(_, sent))
        .getOrElse(throw new IllegalStateException(s"Found no NN for dataId ${cfg.id} and sentence size ${sent.size}"))
    }
  }

  def rate(nn: MultiLayerNetwork, sent: Iterable[String]): Double = {
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

    if (cfg.adjustOutput) cfg.adjustOutputFunc(sent.size, _ratingNN)
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

