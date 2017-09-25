package anagram.ml.data

import java.net.URI

import scala.util.Random

trait WordMapper {


  def toNum(word: String): Int

  def toWord(num: Int): String

  def size: Int

  def randomWord: String

  def containsWord(str: String): Boolean
}

object WordMap {

  private val ran = Random

  def createWordMapFromBooks(books: Seq[URI]): WordMapper = {
    val words: Seq[String] = BookSplitter.sentances(books)
      .flatten
      .toSet
      .toSeq
    val si: Seq[(String, Int)] = words.zipWithIndex

    createMap(si)

  }

  def createWordMapFromWordlist(wordmap: Iterable[String]): WordMapper = {
    val si = wordmap
      .toSeq
      .sorted
      .sortBy(_.length)
      .zipWithIndex
    createMap(si)
  }

  def createWordMapFromWordlistResource(resName: String): WordMapper = {
    val wl = WordList.loadWordList(resName)
    createWordMapFromWordlist(wl)
  }

  private def createMap(si: Seq[(String, Int)]): WordMapper = {
    val siMap = si.toMap
    val is: Seq[(Int, String)] = si.map { case (a, b) => (b, a) }

    val isMap = is.toMap
    val off: Int = siMap.size / 2

    new WordMapper {

      def containsWord(str: String): Boolean = {
        siMap.contains(str)
      }

      def toNum(word: String): Int = siMap(word) - off

      def toWord(num: Int): String = isMap(num + off)

      lazy val size: Int = siMap.size

      def randomWord: String = {
        val i = ran.nextInt(size)
        isMap(i)
      }

    }
  }

}
