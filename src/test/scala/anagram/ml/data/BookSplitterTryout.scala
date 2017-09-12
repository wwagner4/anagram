package anagram.ml.data

object BookSplitterTryout extends App {

  private val books = BookSplitter.booksCommonSense

  val sentances = BookSplitter.sentances(books)
  println(sentances.map(_.mkString(" ")).mkString("\n"))

  val sentances1 = BookSplitter.sentances(books)
  println("-- size:" + sentances1.size)

}
