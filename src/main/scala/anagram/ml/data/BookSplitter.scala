package anagram.ml.data

import java.net.URI

trait BookSplitter {

  def splitSentences(bookUri: URI): Stream[Seq[String]]

}
