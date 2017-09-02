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

  val dataRemoveBlanksOK = List(
    ("", 0, ("", Seq.empty[Int])),
    ("# #", 1, ("##", Seq(1))),
    ("# ##", 1, ("###", Seq(1))),
    ("### # #", 1, ("#### #", Seq(3))),
    ("### # ####", 1, ("### #####", Seq(5))),
    ("### # ####", 2, ("########", Seq(3, 5))),
    ("# ## # ### #", 2, ("# ###### #", Seq(4, 6))),
    ("# # # #", 1, ("# ## #", Seq(3))),
    ("# # # #", 2, ("### #", Seq(1, 3))),
    ("# # # #", 3, ("####", Seq(1, 3, 5))),
  )

  for ((txt, anz, expected) <- dataRemoveBlanksOK) {
    test(s"removeBlanks OK '$txt' $anz") {
      removeBlanks(txt, anz) must be(expected)
    }
  }

  val dataAddBlanksOK = List(
    ("## ##", 1),
    ("# # # #", 1),
    ("# # # #", 2),
    ("# # # #", 3),
    ("##### ##", 1),
    ("## ######", 1),
  )

  for ((txt, anz) <- dataAddBlanksOK) {
    test(s"addBlanks OK '$txt' $anz") {
      val (removed, indices) = removeBlanks(txt, anz)
      addBlanks(removed, indices) must be(txt)
    }
  }

  def removeBlanks(txt: String, numToBeRemoved: Int): (String, Seq[Int]) = {
    val is: Seq[Int] = txt.toList
      .zipWithIndex
      .filter(_._1 == ' ')
      .map(_._2)
    val middle: Int = txt.length / 2
    val idxToBeRemoved = is
      .map(i => (i, math.abs(i - middle)))
      .sortBy(_._2)
      .take(numToBeRemoved)
      .map(_._1)
    val txtOut = txt.zipWithIndex
      .filter(t => !idxToBeRemoved.contains(t._2))
      .map(_._1)
      .mkString("")
    (txtOut, idxToBeRemoved.sorted)
  }

  def addBlanks(txt: String, indexes: Seq[Int]): String = {
    def addBlanks(txt: List[Char], index: Int, indexes: Seq[Int]): List[Char] = {
      txt match {
        case Nil => Nil
        case head :: tail =>
          if (indexes.contains(index)) ' ' :: addBlanks(head :: tail, index + 1, indexes)
          else head :: addBlanks(tail, index + 1, indexes)
      }
    }

    addBlanks(txt.toList, 0, indexes).mkString
  }


}
