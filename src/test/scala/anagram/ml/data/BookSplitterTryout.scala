package anagram.ml.data

object BookSplitterTryout extends App {

  val uris = IoUtil.uris(BookSplitter.booksSmall)

  val sentances = BookSplitter.sentances(uris)
  println(sentances.map(_.mkString(" ")).mkString("\n"))

  val sentances1 = BookSplitter.sentances(uris)
  println("-- size:" + sentances1.size)

}
