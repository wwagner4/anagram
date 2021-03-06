package anagram.ml.data.common

import org.scalatest.{FunSuite, MustMatchers}

class BookSplitterSuite extends FunSuite with MustMatchers {

  private val reName = BookSplitterTxt.bookSmallRes
  private val splitter = new BookSplitterTxt

  test("two lines groups 1") {
    val re = splitter.splitSentences(reName).toList
    re.size mustBe 3
    re(0).mkString(" ") mustBe "them general favor a long habit"
  }

  test("two lines groups 2") {
    val re = splitter.splitSentences(reName).toList
    re.size mustBe 3
    re(1).mkString(" ") mustBe "in the following pages are not yet sufficiently fashionable to procure"
  }

  test("two lines groups 3") {
    val re = splitter.splitSentences(reName).toList
    re.size mustBe 3
    re(2).mkString(" ") mustBe "perhaps the sentiments contained"
  }

}
