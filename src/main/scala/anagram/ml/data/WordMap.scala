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
  private val splitter: BookSplitter = new BookSplitterTxt

  def createWordMapFromBooks(bookUris: Stream[URI]): WordMapper = {
    val allSent: Stream[Seq[String]] = bookUris.flatMap(splitter.splitSentences)

    val words: Seq[String] = bookUris.flatMap(splitter.splitSentences)
      .flatten
      .toSet
      .toSeq
    val si: Seq[(String, Int)] = words.zipWithIndex

    createMap(si)

  }

  def createWordMapFromWordlist(wordlist: Iterable[String]): WordMapper = {
    val grps = wordlist.toSeq.groupBy(w => maxVowel(w)).toSeq
    val si: Seq[Seq[String]] = for ((_, sent) <- grps) yield {
      sent.sorted
    }
    createMap(si.flatten.zipWithIndex)
  }

  def createWordMapFromWordlistResource(resName: String): WordMapper = {
    val wl = WordList.loadWordList(resName)
    createWordMapFromWordlist(wl)
  }

  def maxVowel(word: String): Char = {
    val vowels = Seq('a', 'e', 'i', 'o', 'u')
    val x: Seq[(Char, Int)] = for (v <- vowels) yield {
      val cnt = countChar(word, v)
      (v, cnt)
    }
    val y = x.sortBy(-_._2)
    y(0)._1
  }


  def countChar(word: String, char: Char): Int = {
    val x: Seq[Int] = for (c <- word) yield {
      if (c == char) 1 else 0
    }
    x.sum
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
