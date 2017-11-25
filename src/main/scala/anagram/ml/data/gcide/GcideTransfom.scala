package anagram.ml.data.gcide

import java.nio.file._

import anagram.common.IoUtil
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.io.Codec

case class WordType(txt: String, wtype: String)

object GcideTransfom extends App {

  val log = LoggerFactory.getLogger("GcideTransfom")

  val baseDir = Paths.get(System.getProperty("user.home"), "anagram", "gcide-0.51")
  require(Files.exists(baseDir))

  val cideFiles = Files.newDirectoryStream(baseDir)
    .iterator()
    .asScala
    .toStream
    .filter(_.getFileName.toString.startsWith("CIDE"))

  val outFileName = "wordTypeList.txtSorted"

  log.info("STARTED writing to <workdir>/" + outFileName)
  IoUtil.save(IoUtil.dirWork, outFileName, { bw =>
    val words: Stream[WordType] = cideFiles.flatMap { path =>
      IoUtil.loadTxtFromPath(path, processLines, codec = Codec.ISO8859)
    }
    words.foreach { wt =>
      bw.write("%s;%s%n" format(wt.wtype, wt.txt))
    }
    words.map(wt => wt.wtype).toSet.toList.sorted.foreach(wt => println(wt))
  })
  log.info("FINISHED writing to <workdir>/" + outFileName)

  case class Transp(word: Option[String], words: Seq[WordType])

  def processLines(iter: Iterator[String]): Seq[WordType] = {
    val transp: Transp = iter.toStream.foldLeft(Transp(None, Seq.empty[WordType]))(processLine)
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
        Transp(None, transp.words :+ WordType(transp.word.get, optTyype.get))
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
        val str = line
          .substring(i1 + 5, i2)
          .replaceAll("\\s", "")
          .replaceAll("\\.", "")
          .replaceAll("\\(", "")
          .replaceAll("\\)", "")
          .replaceAll("<or/", ",")
          .replaceAll("<and/", "&")
        if (str.length > 150) None
        else if (str.contains("plain")) None
        else Some(str)
      }
      else None
    }
    else None
  }
}
