package anagram.ml.data

import anagram.common.IoUtil

object BookSplitterTryout extends App {

  val uris = BookSplitter.booksSmall.map(IoUtil.uri)

  val sentances = BookSplitter.sentances(uris)
  println(sentances.map(_.mkString(" ")).mkString("\n"))

  val sentances1 = BookSplitter.sentances(uris)
  println("-- size:" + sentances1.size)

}
