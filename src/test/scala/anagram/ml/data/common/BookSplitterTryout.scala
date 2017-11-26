package anagram.ml.data.common

object BookSplitterTryout extends App {

  val resName = BookSplitterTxt.bookCommonSenseRes
  val splitter = new BookSplitterTxt

  val sentences = splitter.splitSentences(resName)
  println(sentences.map(_.mkString(" ")).mkString("\n"))

  val sentences1 = splitter.splitSentences(resName)
  println("-- size:" + sentences1.size)

}
