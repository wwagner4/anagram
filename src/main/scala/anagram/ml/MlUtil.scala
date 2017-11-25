package anagram.ml

import java.nio.file.{Files, Path}

import anagram.common.DataFile

import scala.collection.JavaConverters._

object MlUtil {

  def getTxtDataFilesFromWorkDir(dir: Path, id: String): Seq[DataFile] = {
    Files.list(dir)
      .iterator().asScala.toStream
      .filter(s => s.getFileName.toString.contains(s"${id}_data_"))
      .map(createDataFile(_, ".*_data_(.*).txt"))
  }

  def getNnDataFilesFromWorkDir(dir: Path, id: String): Seq[DataFile] = {
    Files.list(dir)
      .iterator()
      .asScala
      .toSeq
      .filter(s => s.getFileName.toString.contains(s"${id}_nn"))
      .map(createDataFile(_, ".*_nn_(.*).ser"))
  }

  private def createDataFile(path: Path, regex: String): DataFile = {
    val REG = regex.r
    path.getFileName.toString match {
      case REG(lenStr) => DataFile(lenStr.toInt, path)
      case _ => throw new IllegalArgumentException(s"Could not extract groups length from filename '$path'")
    }
  }



}
