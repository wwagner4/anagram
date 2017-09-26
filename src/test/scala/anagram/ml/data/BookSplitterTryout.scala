package anagram.ml.data

import anagram.common.IoUtil

object BookSplitterTryout extends App {

  val uris = BookSplitter.booksSmall.map(IoUtil.uri)

  val sentences = BookSplitter.sentences(uris)
  println(sentences.map(_.mkString(" ")).mkString("\n"))

  val sentences1 = BookSplitter.sentences(uris)
  println("-- size:" + sentences1.size)

}
