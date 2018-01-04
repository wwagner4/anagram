package anagram.ml.data.common

case class SentenceLabelerCounting(lengthFactors: Map[Int, Double]) extends SentenceLabeler {

  override def labelSentence(sentences: Iterable[Sentence]): Iterable[Labeled] = {
    val rmap: Seq[(Seq[String], Iterable[Sentence])] = sentences.groupBy(sent => sent.words).toSeq
    rmap.flatMap { case (w, sents) =>
      val factor = lengthFactors(w.size)
      if (w.contains("?")) None
      else {
        val y: Seq[(SentenceType, Int)] = sents.map(_.sentenceType).groupBy(identity).mapValues(_.size).toSeq
        val rating: Double = y.foldLeft(0.0) {
          case (r, (stype, cnt)) => stype match {
            case SentenceType_COMPLETE => r + cnt * 1 * factor
            case SentenceType_BEGINNING => r + cnt * 5  * factor
            case SentenceType_OTHER => r + cnt * 10  * factor
            case SentenceType_RANDOM => throw new IllegalStateException("SentenceType_RANDOM makes no sense for Counting")
          }
        }
        Some(Labeled(Sentence(SentenceType_OTHER, w), rating))
      }
    }
  }

}
