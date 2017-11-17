package anagram.solve

import java.nio.file.Path

import anagram.common.IoUtil
import anagram.ml.data.common.{BookCollections, BookSplitterTxt}
import anagram.ml.data.datamodel.grm.WordMappersGrammer
import anagram.ml.data.datamodel.grmred.WordMappersGrammerReduced
import anagram.ml.data.datamodel.plain.WordMappersPlain
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

case class RaterAiCfg(
                       id: String,
                       comonWordRating: Option[Double], // None means no common word rating. Values should be around 0.005
                       mapper: WordMapper,
                       adjustOutput: (Int, Double) => Double,
                     )  {
}


object RaterAiCfgs {

  private def adjustOutputPlain(len: Int, rating: Double): Double = {
    if (len == 1) rating + 5 // Anagram existing of one word must always be top
    else if (len == 2) rating + 3.9
    else if (len == 3) rating + 1.5
    else if (len == 4) rating + 1.2
    else rating
  }

  private def adjustOutputGrammar(len: Int, rating: Double): Double = {
    if (len == 1) rating + 5 // Anagram existing of one word must always be top
    else if (len == 2) rating + 0.2
    else rating
  }

  private def adjustOutputGrammarReduced(len: Int, rating: Double): Double = {
    len match {
      case 1 => rating + 20
      case 2 => rating + 0.6
      case 3 => rating + 0.2
      case 4 => rating + 0.3
      case 5 => rating
      case _ => rating - 20
    }
  }

  def cfgPlain(nr: String) = RaterAiCfg(s"enPlain$nr", Some(0.01), WordMappersPlain.createWordMapper, adjustOutputPlain)
  def cfgGrm(nr: String) = RaterAiCfg(s"enGrm$nr", Some(0.01), WordMappersGrammer.createWordMapper, adjustOutputGrammar)
  def cfgGrmRed = RaterAiCfg(s"GrmRed01", Some(0.005), WordMappersGrammerReduced.createWordMapper, adjustOutputGrammarReduced)


}

class RaterNone extends Rater {

  def rate(sent: Iterable[String]): Double = {
    1.0
  }

}

class RaterAi(cfg: RaterAiCfg, logInterval: Option[Int] = Some(1000)) extends Rater {

  private val log = LoggerFactory.getLogger("RaterAi")

  private lazy val _commonWords = commonWords

  require(logInterval.forall(n => n > 0), "If loginterval is defined it must be greater 0")

  var cnt = 0

  private val nnMap: Map[Int, MultiLayerNetwork] = IoUtil.getNnDataFilesFromWorkDir(cfg.id)
    .map(df => (df.wordLen, deserializeNn(df.path)))
    .toMap

  require(nnMap.nonEmpty, s"Found no NNs for dataId: '${cfg.id}'")

  def rate(sent: Iterable[String]): Double = {
    if (sent.size <= 1) 1.0
    else if (sent.size >= 6) 0.0
    else {
      nnMap.get(sent.size)
        .map(rate(_, sent))
        .getOrElse(throw new IllegalStateException(s"Found no NN for dataId ${cfg.id} and sentence size ${sent.size}"))
    }
  }

  def rate(nn: MultiLayerNetwork, sent: Iterable[String]): Double = {
    logInterval.foreach{interv =>
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
    val _ratingCommoWords = CommonWordRater.rateCommonWords(sent, _commonWords, cfg.comonWordRating)

    cfg.adjustOutput(sent.size, _ratingNN + _ratingCommoWords)

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
      .filter(_._1.length > 3)
      .map { case (w, ws) => (w, ws.size) }
      .sortBy(-_._2)
      .map(_._1)
      .take(2000)
      .toSet
  }

}

object CommonWordRater {

  def rateCommonWords(sent:Iterable[String], commonWords: Set[String], commonFactor: Option[Double]): Double = {
    if (commonFactor.isDefined) {
      sent.map(w => if (commonWords.contains(w)) 1 else 0).sum * commonFactor.get
    } else {
      0.0
    }
  }
}