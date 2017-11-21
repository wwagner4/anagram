package anagram.ml.data.common

import anagram.words.WordMapper

/**
  * Create sliding sentences only if length <= maxLengthSliding.
  */
class SentenceCreatorConditionalSliding extends SentenceCreator {

  val maxLengthSliding = 4

  def create(sentences: Stream[Seq[String]], len: Int, wordMapper: WordMapper): Stream[Sentence] = {
    sentences
      .filter(_.size >= len)
      .map(words => words.flatMap(wordMapper.transform))
      .flatMap(slideSentences(_, len, wordMapper))
  }

  def slideSentences(words: Seq[String], len: Int, wordMapper: WordMapper): Seq[Sentence] = {
    require(words.size >= len)

    if (words.size == len) {
      if (words.forall(wordMapper.containsWord)) {
        Seq(Sentence(SentenceType_COMPLETE, words))
      } else {
        Seq.empty[Sentence]
      }
    } else if (words.length <= maxLengthSliding) {
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