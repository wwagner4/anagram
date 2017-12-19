package anagram.ml.data.common

import anagram.words.WordMapper

class SentenceCreatorSliding(wordMapper: WordMapper) extends SentenceCreator {

  def create(sentences: Stream[Seq[String]], len: Int): Stream[Sentence] = {
    sentences
      .filter(_.lengthCompare(len) >= 0)
      .flatMap(slideSentences(_, len, wordMapper))
  }

  def slideSentences(words: Seq[String], len: Int, wordMapper: WordMapper): Seq[Sentence] = {
    require(words.lengthCompare(len) >= 0)

    if (words.lengthCompare(len) == 0) {
      if (words.forall(wordMapper.containsWord)) {
        Seq(Sentence(SentenceType_COMPLETE, words))
      } else {
        Seq.empty[Sentence]
      }
    } else {
      val ws = words.sliding(len)
        .toList
        .filter(ws => ws.forall(wordMapper.containsWord))
      for ((w, i) <- ws.zipWithIndex) yield {
        if (i == 0) Sentence(SentenceType_BEGINNING, w)
        else Sentence(SentenceType_OTHER, w)
      }
    }
  }

}
