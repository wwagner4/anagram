package anagram

object SentanceMorph {

  import scala.util.Random._

  def split(sentance: String): List[String] = sentance.split(" ").toList

  def morph(words: List[String]): List[String] =
    extraWords(
      interWords(
        flipSpaces(words)
      )
    )

  def flipSpaces(words: List[String]): List[String] =
    words match {
      case Nil => Nil
      case w :: Nil => w :: Nil
      case w1 :: w2 :: rest =>
        nextInt(2) match {
          case 0 if w1.length >= 3 =>
            val w1_last: Char = w1.reverse.head
            val w1_butLast: String = w1.reverse.tail.reverse
            w1_butLast :: (w1_last + w2) :: flipSpaces(rest)
          case 1 if w2.length >= 3 =>
            val w2_first: Char = w2.head
            val w2_butFirst: String = w1.tail
            w1 + w2_butFirst :: w2_butFirst :: flipSpaces(rest)
          case _ => w1 :: w2 :: flipSpaces(rest)
        }
    }

  def interWords(words: List[String]): List[String] = words
  def extraWords(words: List[String]): List[String] = words


  def morph(words: List[String], cnt: Int): List[List[String]] = {
    if (cnt == 0) Nil
    else {
      val m1: List[String] = morph(words)
      m1 :: morph(m1, cnt -1)
    }
  }

  def toRandom(sentance: String, cnt: Int): List[List[String]] = {
    val words: List[String] = split(sentance)
    morph(words, cnt)
  }

}
