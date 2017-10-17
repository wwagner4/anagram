package anagram.ml.data

class SentenceCreatorSliding extends SentenceCreator {

  def create(sentences: Stream[Seq[String]], len: Int, wordMapper: WordMapper): Stream[Sentence] = {
    sentences
      .filter(_.size >= len)
      .flatMap(slideSentences(_, len, wordMapper))
  }

  def slideSentences(groups: Seq[String], len: Int, wordMapper: WordMapper): Seq[Sentence] = {
    require(groups.size >= len)

    if (groups.size == len) {
      if (groups.forall(wordMapper.containsWord)) {
        Seq(Sentence(SentenceType_COMPLETE, groups))
      } else {
        Seq.empty[Sentence]
      }
    } else {
      val ws = groups.sliding(len)
        .toList
        .filter(ws => ws.forall(wordMapper.containsWord))
      for ((w, i) <- ws.zipWithIndex) yield {
        if (i == 0) Sentence(SentenceType_BEGINNING, w)
        else Sentence(SentenceType_OTHER, w)
      }
    }
  }

}
