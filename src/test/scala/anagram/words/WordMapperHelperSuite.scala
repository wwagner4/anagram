package anagram.words

import org.scalatest.{FunSuite, MustMatchers}

class WordMapperHelperSuite extends FunSuite with MustMatchers {

  test("map") {
    val wl = Seq(
      Word("a", "-", None, None),
      Word("a", "-", None, None),
      Word("b", "-", None, None)
    )
    val m = WordMapperHelper.toWordMap(wl)

    //noinspection MapGetGet
    m.get("a").get.word mustBe "a"
    //noinspection MapGetGet
    m.get("b").get.word mustBe "b"
    m.get("c").isDefined mustBe false
  }

}
