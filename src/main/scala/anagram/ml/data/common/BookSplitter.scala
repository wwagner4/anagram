package anagram.ml.data.common

import java.net.URI

trait BookSplitter {

  def splitSentences(bookUri: URI): Stream[Seq[String]]

}
