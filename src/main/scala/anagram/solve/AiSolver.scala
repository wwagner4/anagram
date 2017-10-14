package anagram.solve

import java.nio.file.Path

import anagram.common.IoUtil
import anagram.ml.data.{WordMap, WordMapper}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.util.ModelSerializer
import org.nd4j.linalg.factory.Nd4j
import org.slf4j.LoggerFactory

import scala.util.Random

trait Rater {
  def rate(sent: Iterable[String]): Double
}

case class AiSolver(parentSolver: Solver, rater: Rater) extends Solver {

  def solve(srcText: String, wordlist: Iterable[String]): Stream[Ana] = {
    parentSolver.solve(srcText, wordlist)
      .map(parentAna => Ana(rater.rate(parentAna.sentence) * parentAna.rate, parentAna.sentence))
  }
}

class RandomRater extends Rater {

  def rate(sent: Iterable[String]): Double = {
    Random.nextDouble() * 10
  }

}

class AiRater(dataId: String, wordlist: Iterable[String]) extends Rater {

  private val log = LoggerFactory.getLogger("AiRater")

  var cnt = 0

  private val nnMap: Map[Int, MultiLayerNetwork] = IoUtil.getNnDataFilesFromWorkDir(dataId)
    .map(df => (df.wordLen, deserializeNn(df.path)))
    .toMap

  val wordmap: WordMapper = WordMap.createWordMapFromWordlist(wordlist)

  def rate(sent: Iterable[String]): Double = {
    nnMap.get(sent.size)
      .map(rate(_, sent))
      .getOrElse(0.0)
  }

  def rate(nn: MultiLayerNetwork, sent: Iterable[String]): Double = {
    if (cnt % 50000 == 0) log.info(s"Rated $cnt sentences")
    cnt += 1
    val input: Array[Double] = sent.map(wordmap.toNum(_).toDouble).toArray
    val out = nn.output(Nd4j.create(input))
    out.getDouble(0)
  }

  private def deserializeNn(path: Path): MultiLayerNetwork = {
    ModelSerializer.restoreMultiLayerNetwork(path.toFile)
  }

}
