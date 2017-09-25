package anagram.solve

import java.nio.file.Path

import anagram.common.IoUtil
import anagram.ml.data.{WordList, WordMap, WordMapper}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.util.ModelSerializer
import org.nd4j.linalg.factory.Nd4j

import scala.util.Random

case class Ana(rate: Double, sentance: Iterable[String])

trait Rater {
  def rate(sent: Iterable[String]): Double
}

object AiSolver extends App {

  val id = "en01"
  val wordlist = WordList.loadWordList("wordlist/wordlist_small.txt")

  val rater = new AiRater(id, wordlist)
  //val rater = new RandomRater

  SSolver.solve("klagenfurt", wordlist)
    .map(sent => Ana(rater.rate(sent), sent))
    .sortBy(- _.rate)
    .foreach(ana => println("%10.3f  - '%s'".format(ana.rate, ana.sentance.mkString(" "))))

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
    if (sent.size == 1) 1000.0 else {
      nnMap.get(sent.size)
        .map(rate(_, sent))
        .getOrElse(0.0)
    }
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
