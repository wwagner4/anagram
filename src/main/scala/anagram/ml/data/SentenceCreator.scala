package anagram.ml.data

import java.net.URI

trait SentenceCreator {

  def create(books: Seq[URI], len: Int, wordMapper: WordMapper): Stream[Seq[String]]

}