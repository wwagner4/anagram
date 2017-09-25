package anagram.ml.data

import java.net.URI

object SentanceCreator {

  private val splitter = BookSplitter

  def create(books: Seq[URI], len: Int, wordMapper: WordMapper): Stream[Seq[String]] = {
    splitter.sentances(books)
      .filter(_.size >= len)
      .flatMap(slideSentances(_, len, wordMapper))
  }

  def slideSentances(sent: Seq[String], len: Int, wordMapper: WordMapper): Seq[Seq[String]] = {
    require(sent.size >= len)
    sent.sliding(len)
      .toList
        .filter((sent: Seq[String]) => sent.forall(wordMapper.containsWord))
  }

}
