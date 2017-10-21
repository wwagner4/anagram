package anagram.ml.data

import anagram.common.IoUtil

object BookSplitterTryout extends App {

  val uri = IoUtil.uri(BookSplitterTxt.bookCommonSenseRes)
  val splitter = new BookSplitterTxt

  val sentences = splitter.splitSentences(uri)
  println(sentences.map(_.mkString(" ")).mkString("\n"))

  val sentences1 = splitter.splitSentences(uri)
  println("-- size:" + sentences1.size)

}
