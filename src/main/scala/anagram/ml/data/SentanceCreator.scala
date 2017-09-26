package anagram.ml.data

import java.net.URI

trait SentanceCreator {

  def create(books: Seq[URI], len: Int, wordMapper: WordMapper): Stream[Seq[String]]

}