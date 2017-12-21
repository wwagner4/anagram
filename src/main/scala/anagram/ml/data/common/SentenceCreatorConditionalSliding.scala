package anagram.ml.data.common

import anagram.words.WordMapper

/**
  * Create sliding sentences only if length <= maxLengthSliding.
  */
class SentenceCreatorConditionalSliding(wordMapper: WordMapper[Seq[String]]) extends SentenceCreator {

  val maxLengthSliding = 4

  def create(sentences: Stream[Seq[String]], len: Int): Stream[Sentence] = {
    sentences
      .filter(_.lengthCompare(len) >= 0)
      .flatMap(slideSentences(_, len))
  }

  def slideSentences(words: Seq[String], len: Int): Seq[Sentence] = {
    require(words.lengthCompare(len) >= 0)

    if (words.lengthCompare(len) == 0) {
      if (words.forall(wordMapper.containsWord)) {
        Seq(Sentence(SentenceType_COMPLETE, words))
      } else {
        Seq.empty[Sentence]
      }
    } else if (words.lengthCompare(maxLengthSliding) <= 0) {
      val ws = words.sliding(len)
        .toList
        .filter(ws => ws.forall(wordMapper.containsWord))
      for ((w, i) <- ws.zipWithIndex) yield {
        if (i == 0) Sentence(SentenceType_BEGINNING, w)
        else Sentence(SentenceType_OTHER, w)
      }
    } else {
      Seq.empty[Sentence]
    }
  }

}
