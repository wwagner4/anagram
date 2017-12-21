package anagram.model.plain

import anagram.words.{MappingResult, Word, WordMapper, WordMapperFactory}

class WordMapperFactoryPlain(wl: Iterable[Word]) extends WordMapperFactory[Seq[String]] {

  def create: WordMapper[Seq[String]] = {

    val si: Seq[(String, Int)] = stringInt(wl)
    val siMap = si.toMap
    val off: Int = siMap.size / 2

    new WordMapper[Seq[String]] {

      override def map(sentence: Seq[String]): MappingResult[Seq[String]] = {
        val f = sentence.map(toNum(_).toDouble)
        val i = sentence
        MappingResult(
          intermediate = i,
          features = f,
        )
      }

      override def containsWord(str: String): Boolean = siMap.contains(str)

      private def toNum(word: String): Int = siMap.getOrElse(word, off) - off

      override lazy val size: Int = siMap.size

      override def wordList: Iterable[Word] = wl
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
