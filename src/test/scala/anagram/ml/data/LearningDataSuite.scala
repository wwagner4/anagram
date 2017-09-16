package anagram.ml.data

import org.scalatest.{FunSuite, MustMatchers}

class LearningDataSuite extends FunSuite with MustMatchers {

  val numExchangeData = Seq(

    (2, 100, 0),
    (2, 90, 1),
    (2, 80, 1),
    (2, 70, 1),
    (2, 60, 1),
    (2, 50, 1),
    (2, 40, 1),
    (2, 30, 1),
    (2, 20, 1),
    (2, 10, 1),
    (2, 0, 2),

    (3, 100, 0),
    (3, 90, 1),
    (3, 80, 1),
    (3, 70, 1),
    (3, 60, 2),
    (3, 50, 2),
    (3, 40, 2),
    (3, 30, 2),
    (3, 20, 2),
    (3, 10, 2),
    (3, 0, 3),

    (4, 100, 0),
    (4, 75, 1),
    (4, 50, 2),
    (4, 25, 3),
    (4, 0, 4),
    (5, 100, 0),
    (5, 75, 1),
    (5, 50, 2),
    (5, 25, 3),
    (5, 0, 5),
    (6, 100, 0),
    (6, 75, 1),
    (6, 50, 3),
    (6, 25, 4),
    (6, 0, 6),
    (7, 100, 0),
    (7, 75, 1),
    (7, 50, 3),
    (7, 25, 5),
    (7, 0, 7),
    (8, 100, 0),
    (8, 75, 2),
    (8, 50, 4),
    (8, 25, 6),
    (8, 0, 8),
  )

  for ((len, rating, should) <- numExchangeData) {
    test(s"num exchange $len $rating") {
      LearningData.numExchange(len, rating) mustBe should
    }

  }


}
