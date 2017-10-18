package anagram.ml.data

import scala.util.Random

object WordMapSingleWord {

  private val ran = Random

  def createWordMapperFromWordlist(wordlist: Iterable[String]): WordMapper = {
    val grps = wordlist.toSeq.groupBy(w => maxVowel(w)).toSeq
    val si: Seq[Seq[String]] = for ((_, sent) <- grps) yield {
      sent.sorted
    }
    createMapper(si.flatten.zipWithIndex)
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

  private def createMapper(si: Seq[(String, Int)]): WordMapper = {
    val siMap = si.toMap
    val is: Seq[(Int, String)] = si.map { case (a, b) => (b, a) }

    val isMap = is.toMap
    val off: Int = siMap.size / 2

    new WordMapper {

      override def containsWord(str: String): Boolean = siMap.contains(str)

      override def toNum(word: String): Int = siMap(word) - off

      override def toWord(num: Int): String = isMap(num + off)

      override lazy val  size: Int = siMap.size

      override def randomWord: String = isMap(ran.nextInt(size))

      override def group(value: String) = value
    }
  }

}
