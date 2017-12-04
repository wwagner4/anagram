package anagram.ml.data.common

class SentenceRaterCounting extends SentenceRater {
  override def rateSentence(sentences: Iterable[Sentence]): Iterable[Rated] = {
    val rmap: Seq[(Seq[String], Iterable[Sentence])] = sentences.groupBy(sent => sent.words).toSeq
    rmap.flatMap { case (w, sents) =>
      if (w.contains("?")) None
      else {
        val y: Seq[(SentenceType, Int)] = sents.map(_.sentenceType).groupBy(identity).mapValues(_.size).toSeq
        val rating: Double = y.foldLeft(0.0) {
          case (r, (stype, cnt)) => stype match {
            case SentenceType_COMPLETE => r + cnt * 0.1
            case SentenceType_BEGINNING => r + cnt * 0.05
            case SentenceType_OTHER => r + cnt * 0.001
            case SentenceType_RANDOM => throw new IllegalStateException("SentenceType_RANDOM makes no sense for Counting")
          }
        }
        Some(Rated(Sentence(SentenceType_OTHER, w), rating))
      }
    }
  }
}
