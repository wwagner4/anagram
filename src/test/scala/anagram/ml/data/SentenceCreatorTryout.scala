package anagram.ml.data

import anagram.common.IoUtil

object SentenceCreatorTryout extends App {

  val uris = BookSplitterTxt.booksBig.toStream.map(IoUtil.uri)

  val wm = WordMap.createWordMapFromWordlistResource("wordlist/wordlist_test01.txt")

  val splitter = new BookSplitterTxt

  val split = uris.flatMap(splitter.splitSentences)

  val sent = new SentenceCreatorSliding().create(split, 4, wm)

  println(sent.map(_.words.mkString(" ")).mkString("\n"))

}

