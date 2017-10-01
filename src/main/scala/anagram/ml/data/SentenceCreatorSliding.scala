package anagram.ml.data

import java.net.URI

class SentenceCreatorSliding extends SentenceCreator {

  private val splitter = BookSplitter

  def create(books: Seq[URI], len: Int, wordMapper: WordMapper): Stream[Sentence] = {
    splitter.sentences(books)
      .filter(_.size >= len)
      .flatMap(slideSentences(_, len, wordMapper))
      // TODO Determine Type
      .map(Sentence(SentenceType_OTHER, _))
  }

  def slideSentences(sent: Seq[String], len: Int, wordMapper: WordMapper): Seq[Seq[String]] = {
    require(sent.size >= len)
    sent.sliding(len)
      .toList
        .filter((sent: Seq[String]) => sent.forall(wordMapper.containsWord))
  }

}
