package anagram

case class Anagram(
                    from: String,
                    to: String,
                    lines: Int
                  )


object AnagramMorphJumbled {

  import scala.util.Random._

  val minWordLen = 2
  val probFlipWords = 50 // in %
  val probFlipSpaces = 60 // in %

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

    handlePairs(words, probFlipSpaces)(flipSpaceLeft, flipSpaceRight)
  }

  def flipWords(words: List[String]): List[String] =
    handlePairs(words, prob = probFlipWords)((a, b) => (b, a), (a, b) => (b, a))

  def handlePairs[A](words: List[A], prob: Int)(f1: (A, A) => (A, A), f2: (A, A) => (A, A)): List[A] =
    words match {
      case Nil => Nil
      case w :: Nil => w :: handlePairs(Nil, prob)(f1, f2)
      case w1 :: w2 :: rest =>
        nextInt(nFromP(prob)) match {
          case 0 =>
            val (a, b) = f1(w1, w2)
            a :: handlePairs(b :: rest, prob)(f1, f2)
          case 1 =>
            val (a, b) = f2(w1, w2)
            a :: handlePairs(b :: rest, prob)(f1, f2)
          case _ =>
            w1 :: handlePairs(w2 :: rest, prob)(f1, f2)
        }
    }

  def morph(words: List[String], cnt: Int): List[List[String]] = {
    if (cnt == 0) Nil
    else {
      val m1: List[String] = morph(words)
      m1 :: morph(m1, cnt - 1)
    }
  }

  def nFromP(p: Int): Int =
    if (p <= 0) 100000
    else (200.0 / p.toDouble).toInt

  def toRandom(sentance: String, cnt: Int): List[List[String]] = {
    val words: List[String] = split(sentance)
    split(sentance) :: morph(words, cnt)
  }

  def splitNum(num: Int): (Int, Int) = {
    val n1 = num / 2
    val n2 = num - n1
    (n1, n2)
  }

  def morph(anagram: Anagram): List[List[String]] = {
    val (n1, n2) = splitNum(anagram.lines)
    val a = toRandom(anagram.from, n1)
    val b = toRandom(anagram.to, n2).reverse
    a ::: b
  }

}
