package anagram.ml.data.datamodel.grm

import org.scalatest.{FunSuite, MustMatchers}

class WordMapperFactoryGrammerSuite extends FunSuite with MustMatchers {

  private lazy val m = WordMapperFactoryGrammer.create

  test("transform house") {
    m.transform("house") mustBe Seq("n&vt&vi")
  }

  test("transform table") {
    m.transform("table") mustBe Seq("n&vt&vi")
  }

  test("transform eat") {
    m.transform("eat") mustBe Seq("vt&vi")
  }

  test("transform i") {
    m.transform("i") mustBe Seq("pron")
  }

  test("transform yammiyammi") {
    m.transform("yammiyammi") mustBe Seq("?")
  }
}
