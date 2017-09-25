package anagram.ml.data

import anagram.common.IoUtil

object SentanceCreatorTryout extends App {

  val uris = BookSplitter.booksBig.map(IoUtil.uri)

  val wm = WordMap.createWordMapFromWordlistResource("wordlist/wordlist_test01.txt")

  val sent = SentanceCreator.create(uris, 4, wm)

  println(sent.map(_.mkString(" ")).mkString("\n"))

}

