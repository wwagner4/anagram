package anagram.solve

import java.nio.file.Path

import anagram.common.IoUtil
import anagram.ml.data.{WordMap, WordMapper}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.util.ModelSerializer
import org.nd4j.linalg.exception.ND4JIllegalStateException
import org.nd4j.linalg.factory.Nd4j

import scala.util.Random

case class Ana(rate: Double, sentance: Seq[String])

trait Rater {
  def rate(sent: Seq[String]): Double
}

object AiSolver extends App {
  val id = "en01"

  val dict = IoUtil.getTxtFilePathFromWorkDir(s"${id}_dict")

  val rater = new AiRater(id)

  Solver.solve("ones upon a time", dict)
    .map(sent => Ana(rater.rate(sent), sent))
    .sortBy(- _.rate)
    .foreach(ana => println("%10.3f  - '%s'".format(ana.rate, ana.sentance.mkString(" "))))

}

class RandomRater extends Rater {

  def rate(sent: Seq[String]): Double = {
    Random.nextDouble() * 10
  }

}

class AiRater(dataId: String) extends Rater {

  private val nnMap: Map[Int, MultiLayerNetwork] = IoUtil.getNnDataFilesFromWorkDir(dataId)
    .map(df => (df.wordLen, deserializeNn(df.path)))
    .toMap

  val map: WordMapper = IoUtil.loadTxtFromWorkDir(s"${dataId}_map", WordMap.loadMap)

  def rate(sent: Seq[String]): Double = {
    if (sent.size == 1) 1000.0 else {
      nnMap.get(sent.size)
        .map(rate(_, sent))
        .getOrElse(0.0)
    }
  }

  def rate(nn: MultiLayerNetwork, sent: Seq[String]): Double = {
    val input: Array[Double] = sent.map(map.toNum(_).toDouble).toArray
    val out = nn.output(Nd4j.create(input))
    out.getDouble(0)
  }

  private def deserializeNn(path: Path): MultiLayerNetwork = {
    ModelSerializer.restoreMultiLayerNetwork(path.toFile)
  }

}
