package anagram.impl

import org.scalatest.{FunSuite, MustMatchers}

class LinearSuite extends FunSuite with MustMatchers {

  val classUnderTest = new AnagramMorphLinear()

  val dataOk = List(
    ("abc", "abc", Seq(0, 1, 2)),
    ("abc", "cba", Seq(2, 1, 0)),
    ("abc", "acb", Seq(0, 2, 1)),
    ("abc", "cab", Seq(1, 2, 0)),
    ("abc", "bac", Seq(1, 0, 2)),
    ("aaab", "aaab", Seq(0, 1, 2, 3)),
    ("aaab", "baaa", Seq(1, 2, 3, 0)),
    ("aabba", "abbaa", Seq(0, 3, 1, 2, 4)),
    ("ditschi", "ihcstid", Seq(6, 0, 4, 3, 2, 1, 5)),
    ("noah", "haon", Seq(3, 2, 1, 0)),
  )

  for ((a, b, expected) <- dataOk) {
    test(s"assign OK $a $b") {
      classUnderTest.assign(a, b) must be(expected)
    }
  }

  val dataNotOk = List(
    ("abc", "aac"),
    ("abc", "abcd"),
    ("abc", "abd"),
    ("abc", "ab"),
  )

  for ((a, b) <- dataNotOk) {
    test(s"assign NOT OK $a $b") {
      an[IllegalStateException] must be thrownBy classUnderTest.assign(a, b)
    }
  }



  def morph(target: Seq[Int], numLines: Int): Seq[Seq[Int]] = ???

}
