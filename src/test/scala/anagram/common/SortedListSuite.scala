package anagram.common

import org.scalatest.{FunSuite, MustMatchers}

class SortedListSuite extends FunSuite with MustMatchers {

  test("sorted strings") {
    val sl = SortedList.instance[String]

    sl.add("B")
    sl.add("A")
    sl.add("B")
    sl.add("X")
    sl.add("S")
    sl.add("T")
    sl.add("C")

    sl.take(4) mustBe Seq("A", "B", "B", "C")
    sl.take(2) mustBe Seq("A", "B")

    sl.add("A")
    sl.take(2) mustBe Seq("A", "A")

  }

}
