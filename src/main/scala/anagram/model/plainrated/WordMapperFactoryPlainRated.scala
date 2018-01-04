package anagram.model.plainrated

import anagram.words._

class WordMapperFactoryPlainRated(wl: Iterable[Word]) extends WordMapperFactory[Seq[Double]] {

  def create: WordMapper[Seq[Double]] = {

    val wordToInt: Seq[(String, Int)] = stringInt(wl)
    val wordToIntMap = wordToInt.toMap
    val off: Int = wordToIntMap.size / 2

    val wmap = WordMapperHelper.toWordMap(wl)

    new WordMapper[Seq[Double]] {

      override def map(sentence: Seq[String]): MappingResult[Seq[Double]] = {
        val words = sentence.flatMap( w => toWord(w))
        val i = words.map(w => w.rating.get)
        val f = words.flatMap{w => Seq(
          wordToIntMap(w.word).toDouble,
          w.rating.get
        )}
        MappingResult(
          intermediate = i,
          features = f
        )
      }

      override def toWord(str: String): Option[Word] = wmap.get(str)

    }
  }

  def stringInt(wordlist: Iterable[Word]): Seq[(String, Int)] = {

    def countChar(word: String, char: Char): Int = {
      val x: Seq[Int] = for (c <- word) yield {
        if (c == char) 1 else 0
      }
      x.sum
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

    val grps = wordlist.map(w => w.word).toSeq.groupBy(w => maxVowel(w)).toSeq
    val si: Seq[Seq[String]] = for ((_, sent) <- grps) yield sent.sorted
    si.flatten.zipWithIndex
  }


}
