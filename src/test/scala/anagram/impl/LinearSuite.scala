package anagram.impl

import org.scalatest.{FunSuite, MustMatchers}

class LinearSuite extends FunSuite with MustMatchers {

  val data = List(
    ("abc", "abc", Seq(0, 1, 2)),
    ("abc", "cba", Seq(2, 1, 0)),
    ("abc", "acb", Seq(0, 2, 1)),
    ("abc", "cab", Seq(2, 0, 1)),
    ("abc", "bac", Seq(1, 0, 2))
  )

  for ((a, b, expected) <- data) {
    test(s"assign $a $b") {
      assign(a, b) must be(expected)
    }
  }

  def assign(a: String, b: String): Seq[Int] = {
    Seq(0, 1, 2)
  }

}
