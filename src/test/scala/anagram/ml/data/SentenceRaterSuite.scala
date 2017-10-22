package anagram.ml.data

import anagram.ml.data.analyse.SentenceRaterGenericLengthFactor
import org.scalatest.{FunSuite, MustMatchers}

class SentenceRaterSuite extends FunSuite with MustMatchers {

  val lines = Seq(
    ("119 - 0.33877 - 'tact loin sowed'", 0.33877, 3),
    ("118 - 35.33 - 'tact lion'", 35.33, 2),
    ("120 - 35 - 'weds cotta lino'", 35.0, 3),
    ("121 - 35.33748 - 'weds cotta lion'", 35.33748, 3),
  )

  for ((s, r, l) <- lines) {
    test(s"mkGenRating $s") {
      val rating = SentenceRaterGenericLengthFactor.mkGenRating(s)
      rating.sentLen mustBe l
      rating.rating mustBe r
    }
  }
}
