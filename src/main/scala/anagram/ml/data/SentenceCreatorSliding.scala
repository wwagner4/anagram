package anagram.ml.data

import java.net.URI

class SentenceCreatorSliding extends SentenceCreator {

  private val splitter = BookSplitter

  def create(books: Seq[URI], len: Int, wordMapper: WordMapper): Stream[Sentence] = {
    splitter.sentences(books)
      .filter(_.size >= len)
      .flatMap(slideSentences(_, len, wordMapper))
  }

  def slideSentences(sent: Seq[String], len: Int, wordMapper: WordMapper): Seq[Sentence] = {
    require(sent.size >= len)
    if (sent.size == len) {
      Seq(Sentence(SentenceType_COMPLETE, sent))
    } else {
      val words = sent.sliding(len)
        .toList
        .filter((sent: Seq[String]) => sent.forall(wordMapper.containsWord))
      for ((w, i) <- words.zipWithIndex) yield {
        if (i == 0) Sentence(SentenceType_BEGINNING, w)
        else Sentence(SentenceType_OTHER, w)
      }
    }
  }

}
