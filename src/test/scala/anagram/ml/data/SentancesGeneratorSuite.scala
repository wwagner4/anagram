package anagram.ml.data

import org.scalatest.FunSuite

class SentancesGeneratorSuite extends FunSuite {

  test("Read Books") {
    val splitter = BookSplitter()
    val sentances = splitter.sentances(BookSplitter.books)
    println(sentances.map(_.mkString(" ")).mkString("\n"))
  }

  test("Read Books size") {
    val splitter = BookSplitter()
    val sentances = splitter.sentances(BookSplitter.books)
    println("-- size:" + sentances.size)
  }

}
