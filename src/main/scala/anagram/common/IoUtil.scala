package anagram.common

import java.io.BufferedWriter
import java.nio.file.{Files, Path, Paths}
import java.util.stream.Collectors

import scala.collection.JavaConverters._
import scala.io.Codec

object IoUtil {

  private lazy val _dirAna = Paths.get(System.getProperty("user.home"), "work", "work-anagram")

  def dirAna: Path = {
    getCreateDir(_dirAna)
  }

  def dirWork: Path = {
    getCreateDir(_dirAna.resolve("work"))
  }

  def dirViz: Path = {
    getCreateDir(_dirAna.resolve("viz"))
  }

  def dirVizImages: Path = {
    getCreateDir(dirViz.resolve("images"))
  }

  def dirVizScripts: Path = {
    getCreateDir(dirViz.resolve("scripts"))
  }

  def dirOut: Path = {
    getCreateDir(_dirAna.resolve("out"))
  }

  def dirAnagrams: Path = {
    getCreateDir(_dirAna.resolve("anagrams"))
  }

  private def getCreateDir(p: Path): Path = {
    if (!Files.exists(p)) {
      Files.createDirectories(p)
    }
    p
  }

  def save(dir: Path, fileName: String, f: BufferedWriter => Unit): Path = {
    val d = getCreateDir(dir)
    val file = d.resolve(fileName)
    val wr: BufferedWriter = Files.newBufferedWriter(file)
    try f(wr) finally wr.close()
    file
  }

  def loadTxtFromFile[T](file: Path, f: Iterator[String] => T, codec: Codec = Codec.default): T = {
    val iter = scala.io.Source.fromFile(file.toFile)(codec).getLines()
    f(iter)
  }

  def loadTxtFromResourceName[T](resName: String, f: Iterator[String] => T, codec: Codec = Codec.default): T = {
    val inputStream = getClass.getClassLoader.getResourceAsStream(resName)
    if (inputStream == null) throw new IllegalArgumentException(s"Found no resource for '$resName'")
    val iter = scala.io.Source.fromInputStream(inputStream)(codec).getLines()
    f(iter)
  }

  def allSubdirs(dir: Path): Iterable[Path] = {
    require(Files.exists(dir), s"Directory $dir must exist")
    Files.list(dir)
      .filter(Files.isDirectory(_))
      .collect(Collectors.toList())
      .asScala
  }

  def allFiles(dir: Path): Iterable[Path] = {
    require(Files.exists(dir), s"Directory $dir must exist")
    Files.list(dir)
      .filter(Files.isRegularFile(_))
      .collect(Collectors.toList())
      .asScala
  }

}
