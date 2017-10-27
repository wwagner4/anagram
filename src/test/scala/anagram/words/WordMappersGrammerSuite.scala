package anagram.words

import org.scalatest.{FunSuite, MustMatchers}

class WordMappersGrammerSuite extends FunSuite with MustMatchers {

  private lazy val m = WordMappersGrammer.createWordMapperGrammer

  test("group house") {
    m.group("house") mustBe "n&vt&vi"
  }

  test("group table") {
    m.group("table") mustBe "n&vt&vi"
  }

  test("group eat") {
    m.group("eat") mustBe "vt&vi"
  }

  test("group i") {
    m.group("i") mustBe "pron"
  }

  test("group yammiyammi") {
    m.group("yammiyammi") mustBe "?"
  }
}
