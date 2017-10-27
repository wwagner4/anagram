package anagram.ml.data.analyse

import java.nio.file.{Files, Path, Paths}

import anagram.common.IoUtil

import scala.collection.JavaConverters._

case class DataFileDesc(file: Path, sentenceLength: Int)

case class Stat(len: Int, mean: Double, dev: Double)

object AnalyseRatings extends App {

  val id = "enGrm11"

  lazy val homeDir = System.getProperty("user.home")

  val workdir: Path = Paths.get(homeDir, "anagram", "work")

  require(Files.exists(workdir), s"Workdir $workdir does not exist")
  require(Files.isDirectory(workdir), s"$workdir is not a directory")


  val descs: Seq[DataFileDesc] = Files.list(workdir)
    .iterator
    .asScala
    .toSeq
    .flatMap(toDataFileDesc(id))

  val loc = java.util.Locale.ENGLISH

  descs.map(stat)
    .foreach { stat =>
      val len = stat.len
      val a = "%.4f".formatLocal(loc,  stat.mean)
      val k = "%.4f".formatLocal(loc, stat.dev)
      println(s"    ($len, LinearAdjust($a,$k)),")
    }

  def stat(desc: DataFileDesc): Stat = {
    IoUtil.loadTxtFromPath(desc.file, toStat(desc.sentenceLength))
  }

  def toStat(len: Int)(lines: Iterator[String]): Stat = {
    val ratings = lines.map(toRating).toSeq
    val mean = ratings.sum / ratings.size
    val dev = ratings.map(v => math.pow(mean - v, 2.0)).sum
    val stdDev = math.sqrt(dev / ratings.size)
    Stat(len, mean, stdDev)
  }

  def toRating(line: String): Double = {
    val idx = line.lastIndexOf(";")
    val str = line.substring(idx + 1)
    str.toDouble
  }

  def toDataFileDesc(id: String)(file: Path): Option[DataFileDesc] = {
    val pattern = s"anagram_${id}_data_(.*).txt".r
    file.getFileName.toString match {
      case pattern(len) => Some(DataFileDesc(file, len.toInt))
      case _ => None
    }
  }


}
