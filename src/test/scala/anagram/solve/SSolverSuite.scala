package anagram.solve

import anagram.ml.data.WordList
import org.scalatest.{FunSuite, MustMatchers}

class SSolverSuite extends FunSuite with MustMatchers {

  test("anagramm Oast ogr") {

    val wordlist = List(
      "as",
      "togo",
      "go",
      "r",
    )

    val anas = SSolver(4).solve("Oast ogr", wordlist)

    anas.size mustBe 6

    val strAnas = anas.map(sent => sent.sentence.mkString(" "))

    strAnas must contain("as togo r")
    strAnas must contain("as r togo")
    strAnas must contain("togo as r")
    strAnas must contain("togo r as")
    strAnas must contain("r as togo")
    strAnas must contain("r togo as")

  }

  test("anagramm wolfi with wordlist test01") {

    val wordList =  WordList.loadWordListSmall

    val anas = SSolver(4).solve("wolfi", wordList)

    anas.size mustBe 10

    val strAnas = anas.map(sent => sent.sentence.mkString(" "))

    strAnas must contain("if low")
    strAnas must contain("owl if")
    strAnas must contain("if owl")
    strAnas must contain("low if")

  }

  val validWordData = List(
    ("aaa", "a", Option.empty[String]),
    ("a", "aaa", Some("a")),
    ("a", "a", Some("a")),
    ("a", "abc", Some("a")),
    ("a", "efabc", Some("a")),
    ("ab", "ab", Some("ab")),
    ("ab", "abc", Some("ab")),
    ("ab", "acb", Some("ab")),
    ("ab", "cab", Some("ab")),
    ("ab", "cabc", Some("ab")),
    ("ab", "cacbc", Some("ab")),
    ("ab", "abca", Some("ab")),
    ("ab", "abcxxxx", Some("ab")),
  )

  for ((w, txt, re) <- validWordData) {
    test(s"validWord $w $txt") {
      SSolver(4).validWord(w, txt) mustBe re
    }
  }

  for ((w, txt, re) <- validWordData) {
    test(s"validWord1 $w $txt") {
      SSolver(4).validWord(w, txt.sorted) mustBe re
    }
  }

  val removeFirstData = List(
    ('a', "a", 0, ""),
    ('a', "ab", 0, "b"),
    ('a', "ba", 1, "b"),
  )

  for ((c, s, i, re) <- removeFirstData) {
    test(s"removeFirst $c $s") {
      SSolver(4).removeFirst(c, s, i) mustBe re
    }
  }

}
