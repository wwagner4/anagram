package anagram.ml.data.common

class SentenceCreatorSliding extends SentenceCreator {

  def create(sentences: Stream[Seq[String]], len: Int): Stream[Sentence] = {
    sentences
      .filter(_.lengthCompare(len) >= 0)
      .flatMap(slideSentences(_, len))
  }

  def slideSentences(words: Seq[String], len: Int): Seq[Sentence] = {
    require(words.lengthCompare(len) >= 0)

    if (words.lengthCompare(len) == 0) {
      Seq(Sentence(SentenceType_COMPLETE, words))
    } else {
      val ws = words.sliding(len)
        .toList
      for ((w, i) <- ws.zipWithIndex) yield {
        if (i == 0) Sentence(SentenceType_BEGINNING, w)
        else Sentence(SentenceType_OTHER, w)
      }
    }
  }

}
