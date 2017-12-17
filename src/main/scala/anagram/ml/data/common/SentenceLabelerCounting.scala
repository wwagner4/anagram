package anagram.ml.data.common

import anagram.words.WordTransformer

case class SentenceLabelerCounting(lengthFactors: Map[Int, Double], wt: WordTransformer) extends SentenceLabeler {

  override def labelSentence(sentences: Iterable[Sentence]): Iterable[Labeled] = {
    val s1 = sentences.map ( s => s.copy(words = s.words.flatMap(wt.transform)) )
    val rmap: Seq[(Seq[String], Iterable[Sentence])] = s1.groupBy(sent => sent.words).toSeq
    rmap.flatMap { case (words, sents) =>
      val factor = lengthFactors(words.size)
      if (words.contains("?")) None
      else {
        val y: Seq[(SentenceType, Int)] = sents.map(_.sentenceType).groupBy(identity).mapValues(_.size).toSeq
        val rating: Double = y.foldLeft(0.0) {
          case (r, (stype, cnt)) => stype match {
            case SentenceType_COMPLETE => r + cnt * 1 * factor
            case SentenceType_BEGINNING => r + cnt * 5 * factor
            case SentenceType_OTHER => r + cnt * 10 * factor
            case SentenceType_RANDOM => throw new IllegalStateException("SentenceType_RANDOM makes no sense for Counting")
          }
        }
        Some(Labeled(Sentence(SentenceType_OTHER, words), features(words), rating))
      }
    }
  }

  def features(sent: Seq[String]): Seq[Double] = sent.map(wt.toNum)
}
