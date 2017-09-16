package anagram.ml.data

import java.io.BufferedWriter
import java.net.URI

import scala.util.Random

trait WordMapper {

  def toNum(word: String): Int

  def toWord(num: Int): String

  def writeMap(wr: BufferedWriter): Unit

  def writeDict(wr: BufferedWriter): Unit

  def size: Int

  def randomWord: String

}

object WordMap {

  private val ran = Random

  def createWordMap(books: Seq[URI]): WordMapper = {
    val words: Seq[String] = BookSplitter.sentances(books)
      .flatten
      .toSet
      .toSeq
    val si: Seq[(String, Int)] = words.zipWithIndex
    val is: Seq[(Int, String)] = si.map { case (a, b) => (b, a) }

    val siMap = si.toMap
    val isMap = is.toMap

    new WordMapper {

      def toNum(word: String): Int = siMap(word)

      def toWord(num: Int): String = isMap(num)

      lazy val size: Int = siMap.size

      def randomWord: String = {
        val i = ran.nextInt(size)
        isMap(i)
      }

      def writeMap(wr: BufferedWriter): Unit = {
        for ((s, i) <- isMap.iterator) {
          wr.write(s"$s $i\n")
        }
      }

      def writeDict(wr: BufferedWriter): Unit = {
        for (word <- isMap.values.toList.sorted.iterator) {
          wr.write(s"$word\n")
        }
      }

    }

  }

  def loadMap(lines: Iterator[String]): Map[String, Int] = {

    def lineToTuple(line: String): (String, Int) = {
      val sp = line.split("\\s")
      (sp(0), sp(1).toInt)
    }

    lines.toStream.map(lineToTuple).toMap
  }

}
