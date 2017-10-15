package anagram.ml.data.gcide

import java.nio.file._

import anagram.common.IoUtil

import scala.collection.JavaConverters._
import scala.io.Codec

case class Word(txt: String, wtype: String)

object GcideTransfom extends App {

  val baseDir = Paths.get(System.getProperty("user.home"), "anagram", "gcide-0.51")
  require(Files.exists(baseDir))

  val ps1 = Files.newDirectoryStream(baseDir)
    .iterator()
    .asScala
    .toSeq
    .filter(_.getFileName.toString.startsWith("CIDE"))

  val words: Seq[Word] = ps1.flatMap { path =>
    IoUtil.loadTxtFromPath(path, processLines, codec = Codec.ISO8859)
  }

  words.foreach(w => println("'%s' - %s" format(w.wtype, w.txt)))

  case class Transp(word: Option[String], words: Seq[Word])

  def processLines(iter: Iterator[String]): Seq[Word] = {
    val transp: Transp = iter.toStream.foldLeft(Transp(None, Seq.empty[Word]))(processLine)
    transp.words
  }

  def processLine(transp: Transp, line: String): Transp = {
    val optWord = extractWord(line)
    if (optWord.isDefined) {
      Transp(Some(optWord.get), transp.words)
    }
    else {
      val optTyype = extractWtype(line)
      if (optTyype.isDefined && transp.word.isDefined) {
        Transp(None, transp.words :+ Word(transp.word.get, optTyype.get))
      }
      else {
        Transp(None, transp.words)
      }
    }
  }

  def extractWord(line: String): Option[String] = {
    val i1 = line.indexOf("<ent>")
    if (i1 >= 0) {
      val i2 = line.indexOf("</ent>", i1 + 1)
      if (i2 >= 0) {
        val str = line.substring(i1 + 5, i2).toLowerCase
        if (str.toCharArray.forall(c => Character.isAlphabetic(c))) Some(str)
        else None
      }
      else None
    }
    else None
  }

  def extractWtype(line: String): Option[String] = {
    val i1 = line.indexOf("<pos>")
    if (i1 >= 0) {
      val i2 = line.indexOf("</pos>", i1 + 1)
      if (i2 >= 0) {
        val str = line.substring(i1 + 5, i2)
        Some(str)
      }
      else None
    }
    else None
  }
}
