package anagram.solve

import anagram.common.IoUtil

import scala.util.Random

case class Ana(rate: Double, sentance: Seq[String])

trait Rater {
  def rate(sent: Seq[String]): Double
}

object AiSolver extends App {

  val id = "en01"

  val dict = IoUtil.getTxtFilePathFromWorkDir(s"${id}_dict")

  val rater = new AiRater()

  Solver.solve("ones upon a time", dict)
    .map(sent => Ana(rater.rate(sent), sent))
    .sortBy(- _.rate)
    .foreach(ana => println("%.3f %s".format(ana.rate, ana.sentance.mkString(" "))))

}

class RandomRater extends Rater {

  def rate(sent: Seq[String]): Double = {
    Random.nextDouble() * 10
  }

}

class AiRater extends Rater {

  def rate(sent: Seq[String]): Double = {
    Random.nextDouble() * 10
  }

}
