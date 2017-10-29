package anagram.solve

import anagram.words.{Word, WordMappers}
import org.scalatest.{FunSuite, MustMatchers}

class SolverImplSuite extends FunSuite with MustMatchers {

  test("anagramm Oast ogr") {

    val wordlist = List(
      "as",
      "togo",
      "go",
      "r",
    ).map(toWord)

    val anas = SolverImpl(4, 2).solve("Oast ogr", wordlist)

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
    val wordList = WordMappers.createWordMapperPlain.wordList
    val anas = SolverImpl(4, 2).solve("wolfi", wordList)
    anas.size mustBe 10

    val strAnas = anas.map(sent => sent.sentence.mkString(" "))

    strAnas must contain("if low")
    strAnas must contain("owl if")
    strAnas must contain("if owl")
    strAnas must contain("low if")

  }

  test("anagramm iset with wordlist test01") {
    val wordList = WordMappers.createWordMapperPlain.wordList
    val anas = SolverImpl(4, 2).solve("iset", wordList)

    val strAnas = anas.map(sent => sent.sentence.mkString(" "))

    strAnas must contain("i set")
    strAnas must contain("set i")
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
    ("ab", "abcx", Some("ab")),
    ("ab", "abcb", Some("ab")),
    ("ab", "abcabcabcabc", Some("ab")),
    ("ab", "cabcabcabcabc", Some("ab")),
    ("ab", "abcxxxx", Some("ab")),
    ("ba", "abcxxxx", Some("ab")),
    ("rs", "abcxxxx", Option.empty[String]),
    ("oooo", "abcxxxx", Option.empty[String]),
    ("bcdefghijklmn", "a", Option.empty[String]),
  )

  for ((w, txt, re) <- validWordData) {
    test(s"validWordFromSorted $w $txt") {
      sorted(SolverImpl(4, 2).validWordFromSorted(toWord(w), txt.sorted)) mustBe re
    }
  }

  val removeFirstData = List(
    ('a', "a", 0, ""),
    ('a', "ab", 0, "b"),
    ('a', "ba", 1, "b"),
  )

  for ((c, s, i, re) <- removeFirstData) {
    test(s"removeFirst $c $s") {
      SolverImpl(4, 3).removeFirst(c, s, i) mustBe re
    }
  }

  private def sorted(in: Option[String]): Option[String] = in.map(str => str.sorted)

  private def toWord(str: String): Word = Word(str, str.sorted)
}
