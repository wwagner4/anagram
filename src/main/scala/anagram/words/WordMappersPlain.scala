package anagram.words

import java.nio.file.Paths

import anagram.common.IoUtil

import scala.util.Random

object WordMappersPlain extends WordMappersAbstract {

  private val ran = Random

  def createWordMapperPlain: WordMapper = {

    val wordlist: Iterable[Word] =
      loadWordList("wordlist/wordlist_small.txt")
        .map(line => Word(line, line.sorted))


    val si: Seq[(String, Int)] = stringInt(wordlist)
    val siMap = si.toMap
    val is: Seq[(Int, String)] = si.map { case (a, b) => (b, a) }

    val isMap = is.toMap
    val off: Int = siMap.size / 2

    new WordMapper {

      override def containsWord(str: String): Boolean = siMap.contains(str)

      override def toNum(word: String): Int = siMap(word) - off

      override def toWord(num: Int): String = isMap(num + off)

      override lazy val size: Int = siMap.size

      override def randomWord: String = isMap(ran.nextInt(size))

      override def transform(value: String): Seq[String] = Seq(value)

      override def wordList: Iterable[Word] = wordlist
    }
  }

  def stringInt(wordlist: Iterable[Word]): Seq[(String, Int)] = {
    val grps = wordlist.map(w => w.word).toSeq.groupBy(w => maxVowel(w)).toSeq
    val si: Seq[Seq[String]] = for ((_, sent) <- grps) yield {
      sent.sorted
    }
    si.flatten.zipWithIndex
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

  private def loadWordList(resName: String): Iterable[String] =
    IoUtil.loadTxtFromPath(Paths.get(IoUtil.uri(resName)), (l) => l.toIterable)
}
