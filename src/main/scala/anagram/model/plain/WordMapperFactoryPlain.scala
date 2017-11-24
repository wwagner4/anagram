package anagram.model.plain

import anagram.words.{Word, WordMapper, Wordlists}

import scala.util.Random

object WordMapperFactoryPlain {

  private val ran = Random

  def create: WordMapper = {

    val wordlist: Iterable[Word] = Wordlists.plain.wordList()

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

}
