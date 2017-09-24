package anagram.solve

import java.nio.file.Paths

import anagram.common.IoUtil
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

    val anas = SSolver.solve("Oast ogr", wordlist)

    anas.size mustBe 6

    val strAnas: Seq[String] = anas.map(sent => sent.mkString(" "))

    strAnas must contain("as togo r")
    strAnas must contain("as r togo")
    strAnas must contain("togo as r")
    strAnas must contain("togo r as")
    strAnas must contain("r as togo")
    strAnas must contain("r togo as")

  }

  test("anagramm wolfi with wordlist test01") {

    val wordList = WordList.loadWordList(Paths.get(IoUtil.uri("wordlist/wordlist_test01.txt")))

    val anas = SSolver.solve("wolfi", wordList)

    anas.size mustBe 4

    val strAnas: Seq[String] = anas.map(sent => sent.mkString(" "))

    strAnas must contain("if low")
    strAnas must contain("owl if")
    strAnas must contain("if owl")
    strAnas must contain("low if")

  }

}
