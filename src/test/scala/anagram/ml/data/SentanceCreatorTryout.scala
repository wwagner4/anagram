package anagram.ml.data

import anagram.common.IoUtil

object SentanceCreatorTryout extends App {

  val uris = BookSplitter.booksBig.map(IoUtil.uri)

  val sent = SentanceCreator.create(uris, 4)

  println(sent.map(_.mkString(" ")).mkString("\n"))

}

