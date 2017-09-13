package anagram.ml.data

object SentanceCreator {

  private val splitter = BookSplitter

  def create(books: Seq[Book], len: Int): Stream[Seq[String]] = {
    splitter.sentances(books)
      .filter(_.size >= len)
      .flatMap(slideSentances(_, len))
  }

  def slideSentances(sent: Seq[String], len: Int): Seq[Seq[String]] = {
    require(sent.size >= len)
    sent.sliding(len).toList
  }

}
