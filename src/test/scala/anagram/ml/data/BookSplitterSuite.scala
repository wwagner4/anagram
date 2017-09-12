package anagram.ml.data

import org.scalatest.FunSuite

class BookSplitterSuite extends FunSuite {

  ignore("Read Books") {
    val sentances = BookSplitter().sentances(BookSplitter.books)
    println(sentances.map(_.mkString(" ")).mkString("\n"))
  }

  ignore("Read Books size") {
    val sentances = BookSplitter().sentances(BookSplitter.books)
    println("-- size:" + sentances.size)
  }

}
