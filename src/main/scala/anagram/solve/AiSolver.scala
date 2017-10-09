package anagram.solve

import java.nio.file.Path

import anagram.common.IoUtil
import anagram.ml.data.{WordMap, WordMapper}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.util.ModelSerializer
import org.nd4j.linalg.factory.Nd4j

import scala.collection.GenIterable
import scala.util.Random

case class Ana(rate: Double, sentence: Iterable[String])

trait Rater {
  def rate(sent: Iterable[String]): Double
}

object AiSolver {

  def solve(srcText: String, wordlist: Iterable[String], rater: Rater): GenIterable[Ana] = {
    SSolver().solve(srcText, wordlist)
      .map(sent => Ana(rater.rate(sent), sent))
  }
}

class RandomRater extends Rater {

  def rate(sent: Iterable[String]): Double = {
    Random.nextDouble() * 10
  }

}

class AiRater(dataId: String, wordlist: Iterable[String]) extends Rater {

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
    val input: Array[Double] = sent.map(wordmap.toNum(_).toDouble).toArray
    val out = nn.output(Nd4j.create(input))
    out.getDouble(0)
  }

  private def deserializeNn(path: Path): MultiLayerNetwork = {
    ModelSerializer.restoreMultiLayerNetwork(path.toFile)
  }

}
