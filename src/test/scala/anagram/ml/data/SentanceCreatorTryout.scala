package anagram.ml.data

object SentanceCreatorTryout extends App {

  val sent = SentanceCreator.create(BookSplitter.booksCommonSense, 4)

  println(sent.map(_.mkString(" ")).mkString("\n"))

}

