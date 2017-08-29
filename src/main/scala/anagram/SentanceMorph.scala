package anagram

object SentanceMorph {

  import scala.util.Random._

  val minWordLen = 2

  def split(sentance: String): List[String] = sentance.split(" ").toList

  def morph(words: List[String]): List[String] =
    flipWords(
      flipSpaces(words)
    )

  def flipSpaces(words: List[String]): List[String] = {
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

    handlePairs(words)(flipSpaceLeft, flipSpaceRight)
  }

  def flipWords(words: List[String]): List[String] =
    handlePairs(words, prob = 20)((a, b) => (b, a), (a, b) => (b, a))

  def handlePairs[A](words: List[A], prob: Int = 2)(f1: (A, A) => (A, A), f2: (A, A) => (A, A)): List[A] =
    words match {
      case Nil => Nil
      case w :: Nil => w :: handlePairs(Nil)(f1, f2)
      case w1 :: w2 :: rest =>
        nextInt(prob) match {
          case 0 =>
            val (a, b) = f1(w1, w2)
            a :: handlePairs(b :: rest)(f1, f2)
          case 1 =>
            val (a, b) = f2(w1, w2)
            a :: handlePairs(b :: rest)(f1, f2)
          case _ =>
            w1 :: handlePairs(w2 :: rest)(f1, f2)
        }
    }

  def morph(words: List[String], cnt: Int): List[List[String]] = {
    if (cnt == 0) Nil
    else {
      val m1: List[String] = morph(words)
      m1 :: morph(m1, cnt - 1)
    }
  }

  def toRandom(sentance: String, cnt: Int): List[List[String]] = {
    val words: List[String] = split(sentance)
    split(sentance) :: morph(words, cnt)
  }

}
