package anagram.common

import java.io.BufferedWriter
import java.net.URI
import java.nio.file.{Files, Path, Paths}

import scala.collection.JavaConverters._

/**
  * Description for a datafile
  */
case class DataFile(
                     wordLen: Int,
                     path: Path,
                   )

object IoUtil {

  def saveMapToWorkDir(id: String, f: BufferedWriter => Unit): Path = {
    saveTxtToWorkDir(s"${id}_map", f)
  }

  def loadMapFromWorkDir[T](id: String, f: Iterator[String] => T): T = {
    loadTxtFromWorkDir(s"${id}_map", f)
  }

  def saveDataToWorkDir(id: String, sentancelength: Int, f: BufferedWriter => Unit): Path = {
    saveTxtToWorkDir(s"${id}_data_$sentancelength", f)
  }

  def loadTxtFromPath[T](path: Path, f: Iterator[String] => T): T = {
    val iter = scala.io.Source.fromFile(path.toFile).getLines()
    f(iter)
  }

  def getTxtDataFilesFromWorkDir(id: String): Seq[DataFile] = {
    Files.list(getCreateWorkDir)
      .iterator().asScala.toStream
      .filter(s => s.getFileName.toString.contains(s"${id}_data_"))
      .map(createDataFile(_, ".*_data_(.*).txt"))
  }

  def getNnDataFilesFromWorkDir(id: String): Seq[DataFile] = {
    Files.list(IoUtil.getCreateWorkDir)
      .iterator()
      .asScala
      .toSeq
      .filter(s => s.getFileName.toString.contains(s"${id}_nn"))
      .map(createDataFile(_, ".*_nn_(.*).ser"))
  }

  def nnDataFilePath(id: String, sentanceLength: Int): Path = {
    getCreateWorkDir.resolve(s"anagram_${id}_nn_$sentanceLength.ser")
  }

  def uri(res: String): URI = {
    val url = getClass.getClassLoader.getResource(res)
    if (url == null) throw new IllegalArgumentException(s"Illegal URL '$res'")
    url.toURI
  }

  private def getCreateWorkDir: Path = {
    val dirWork: Path = Paths.get(System.getProperty("user.home"), "anagram", "work")
    if (!Files.exists(dirWork)) {
      Files.createDirectories(dirWork)
    }
    dirWork
  }

  private def save(dir: Path, fileName: String, f: BufferedWriter => Unit): Path = {
    val file = dir.resolve(fileName)
    val wr: BufferedWriter = Files.newBufferedWriter(file)
    try f(wr) finally wr.close()
    file
  }

  private def saveTxtToWorkDir(id: String, f: BufferedWriter => Unit): Path = {
    val filename = s"anagram_$id.txt"
    save(getCreateWorkDir, filename, f)
  }

  private def loadTxtFromWorkDir[T](id: String, f: Iterator[String] => T): T = {
    val fileName = s"anagram_$id.txt"
    val file = getCreateWorkDir.resolve(fileName)
    loadTxtFromPath(file, f)
  }

  private def createDataFile(path: Path, regex: String): DataFile = {
    val REG = regex.r
    path.getFileName.toString match {
      case REG(lenStr) => DataFile(lenStr.toInt, path)
      case _ => throw new IllegalArgumentException(s"Could not extract sentance length from filename '$path'")
    }
  }

}
