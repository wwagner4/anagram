package anagram.solve

import org.scalatest.{FunSuite, MustMatchers}

class SSolverSuite extends FunSuite with MustMatchers {

  test("simple") {


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
    strAnas must contain("as togo r")
    strAnas must contain("as r togo")
    strAnas must contain("togo as r")
    strAnas must contain("togo r as")
    strAnas must contain("r as togo")
    strAnas must contain("r togo as")

  }

}
