package anagram.impl

import org.scalatest.{FunSuite, MustMatchers}

import scala.collection.immutable

class LinearSuite extends FunSuite with MustMatchers {

  val classUnderTest = new AnagramMorphLinear()

  val dataAssignOk = List(
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

  for ((a, b, expected) <- dataAssignOk) {
    test(s"assign OK $a $b") {
      classUnderTest.assign(a, b) must be(expected)
    }
  }

  val dataAssignNotOk = List(
    ("abc", "aac"),
    ("abc", "abcd"),
    ("abc", "abd"),
    ("abc", "ab"),
  )

  for ((a, b) <- dataAssignNotOk) {
    test(s"assign NOT OK $a $b") {
      an[IllegalStateException] must be thrownBy classUnderTest.assign(a, b)
    }
  }

  val dataMorphOk = Seq(
    (Seq(3, 0, 1, 2), 4, Seq(
      Seq(0, 1, 2, 3),
      Seq(0, 1, 3, 2),
      Seq(0, 3, 1, 2),
      Seq(3, 0, 1, 2),
    )),
    (Seq(0, 2, 3, 1), 5, Seq(
      Seq(0, 1, 2, 3),
      Seq(0, 1, 2, 3),
      Seq(0, 2, 1, 3),
      Seq(0, 2, 3, 1),
      Seq(0, 2, 3, 1),
    )),
    (Seq(2, 1, 0), 3, Seq(
      Seq(0, 1, 2),
      Seq(0, 1, 2),
      Seq(2, 1, 0),
    )),
  )

  for ((a, num, expected) <- dataMorphOk) {
    test(s"morph OK ${a.mkString("[", ",", "]")}") {
      classUnderTest.morphIndex(a, num) must be(expected)
    }
  }

}
