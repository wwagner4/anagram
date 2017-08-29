package anagram

object SentanceMorph {

  import scala.util.Random._

  val minWordLen = 2

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
      case w :: Nil => w :: flipSpaces(Nil)
      case w1 :: w2 :: rest =>
        nextInt(2) match {
          case 0 =>
            val (a, b) = flipSpaceLeft(w1, w2)
            a :: flipSpaces(b :: rest)
          case 1 =>
            val (a, b) = flipSpaceRight(w1, w2)
            a :: flipSpaces(b :: rest)
          case _ =>
            w1 :: flipSpaces(w2 :: rest)
        }
    }

  def flipSpaceRight(w1: String, w2: String): (String, String) = {
    if (w1.length > minWordLen) {
      val x = w1.reverse.head
      (w1.reverse.tail.reverse, x + w2)
    } else {
      (w1, w2)
    }
  }

  def flipSpaceLeft(w1: String, w2: String): (String, String) = {
    if (w2.length > minWordLen) {
      val x = w2.head
      (w1 + x, w2.tail)
    } else {
      (w1, w2)
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
