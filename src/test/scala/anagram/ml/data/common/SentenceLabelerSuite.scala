package anagram.ml.data.common

import anagram.ml.data.analyze.SentenceLengthRatingDiff
import org.scalatest.{FunSuite, MustMatchers}

class SentenceLabelerSuite extends FunSuite with MustMatchers {

  val lines = Seq(
    ("119 - 0.33877 - 'tact loin sowed'", 0.33877, 3),
    ("118 - 35.33 - 'tact lion'", 35.33, 2),
    ("120 - 35 - 'weds cotta lino'", 35.0, 3),
    ("121 - 35.33748 - 'weds cotta lion'", 35.33748, 3),
  )

  for ((s, r, l) <- lines) {
    test(s"mkGenRating $s") {
      val rating = SentenceLengthRatingDiff.mkGenRating(s)
      rating.sentLen mustBe l
      rating.rating mustBe r
    }
  }
}
