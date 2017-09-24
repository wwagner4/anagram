package anagram.ml.data

import anagram.common.IoUtil
import org.scalatest.{FunSuite, MustMatchers}

class BookSplitterSuite extends FunSuite with MustMatchers {

  private val uris = BookSplitter.booksSmall.map(IoUtil.uri)

  test("two lines sent 1") {
    val re = BookSplitter.sentances(uris).toList
    re.size mustBe 3
    re(0).mkString(" ") mustBe "them general favor a long habit"
  }

  test("two lines sent 2") {
    val re = BookSplitter.sentances(uris).toList
    re.size mustBe 3
    re(1).mkString(" ") mustBe "in the following pages are not yet sufficiently fashionable to procure"
  }

  test("two lines sent 3") {
    val re = BookSplitter.sentances(uris).toList
    re.size mustBe 3
    re(2).mkString(" ") mustBe "perhaps the sentiments contained"
  }

}
