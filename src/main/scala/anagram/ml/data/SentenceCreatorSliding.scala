package anagram.ml.data

import java.net.URI

class SentenceCreatorSliding extends SentenceCreator {

  private val splitter = BookSplitter

  def create(books: Seq[URI], len: Int, wordMapper: WordMapper): Stream[Sentence] = {
    splitter.sentences(books)
      .filter(_.size >= len)
      .flatMap(slideSentences(_, len, wordMapper))
  }

  def slideSentences(words1: Seq[String], len: Int, wordMapper: WordMapper): Seq[Sentence] = {
    require(words1.size >= len)

    if (words1.size == len) {
      if (words1.forall(wordMapper.containsWord)) {
        Seq(Sentence(SentenceType_COMPLETE, words1))
      } else {
        Seq.empty[Sentence]
      }
    } else {
      val words = words1.sliding(len)
        .toList
        .filter(ws => ws.forall(wordMapper.containsWord))
      for ((w, i) <- words.zipWithIndex) yield {
        if (i == 0) Sentence(SentenceType_BEGINNING, w)
        else Sentence(SentenceType_OTHER, w)
      }
    }
  }

}
