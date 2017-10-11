package anagram.ml.data

import anagram.common.IoUtil
import org.scalatest.{FunSuite, MustMatchers}

class BookSplitterSuite extends FunSuite with MustMatchers {

  private val uri = IoUtil.uri(BookSplitterTxt.bookSmallRes)
  private val splitter = new BookSplitterTxt

  test("two lines words 1") {
    val re = splitter.splitSentences(uri).toList
    re.size mustBe 3
    re(0).mkString(" ") mustBe "them general favor a long habit"
  }

  test("two lines words 2") {
    val re = splitter.splitSentences(uri).toList
    re.size mustBe 3
    re(1).mkString(" ") mustBe "in the following pages are not yet sufficiently fashionable to procure"
  }

  test("two lines words 3") {
    val re = splitter.splitSentences(uri).toList
    re.size mustBe 3
    re(2).mkString(" ") mustBe "perhaps the sentiments contained"
  }

}
