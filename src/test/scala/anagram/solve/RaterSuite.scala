package anagram.solve

import org.scalatest.{FunSuite, MustMatchers}

class RaterSuite extends FunSuite with MustMatchers {


  test("commonWords wolfi") {
    val sent = Seq("a", "b", "wolfi")
    val cw = Seq("wolfi").toSet
    CommonWordRater.rateCommonWords(sent, cw, 1.0) mustBe 1.0
  }

  test("commonWords a b") {
    val sent = Seq("a", "b", "wolfi")
    val cw = Seq("a", "b").toSet
    CommonWordRater.rateCommonWords(sent, cw, 0.1) mustBe 0.2
  }

}
