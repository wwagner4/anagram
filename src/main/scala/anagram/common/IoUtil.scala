package anagram.common

import java.io.BufferedWriter
import java.net.URI
import java.nio.file.{Files, Path, Paths}

import scala.io.Codec

/**
  * Description for a datafile
  */
case class DataFile(
                     wordLen: Int,
                     path: Path,
                   )

object IoUtil {

  def loadTxtFromFile[T](path: Path, f: Iterator[String] => T, codec: Codec = Codec.default): T = {
    val iter = scala.io.Source.fromFile(path.toFile)(codec).getLines()
    f(iter)
  }

  def loadTxtFromWorkdir[T](fileName: String, f: Iterator[String] => T): T = {
    val file = dirWork.resolve(fileName)
    loadTxtFromFile(file, f)
  }


  def nnDataFilePath(id: String, sentenceLength: Int): Path = {
    dirWork.resolve(s"anagram_${id}_nn_$sentenceLength.ser")
  }

  def uri(res: String): URI = {
    val url = getClass.getClassLoader.getResource(res)
    if (url == null) throw new IllegalArgumentException(s"Illegal URL '$res'")
    url.toURI
  }

  private lazy val _dirAna = Paths.get(System.getProperty("user.home"), "anagram")

  def getCreateDirRelative(pathRelative: Path): Path = {
    val dir = _dirAna.resolve(pathRelative)
    getCreateDir(dir)
  }

  def dirWork: Path = {
    getCreateDir(_dirAna.resolve("work"))
  }

  def dirAna: Path = {
    getCreateDir(_dirAna)
  }

  def getCreateDirOut: Path = {
    getCreateDir(_dirAna.resolve("out"))
  }

  private def getCreateDir(p: Path): Path = {
    if (!Files.exists(p)) {
      Files.createDirectories(p)
    }
    p
  }

  def save(dir: Path, fileName: String, f: BufferedWriter => Unit): Path = {
    val file = dir.resolve(fileName)
    val wr: BufferedWriter = Files.newBufferedWriter(file)
    try f(wr) finally wr.close()
    file
  }
}
