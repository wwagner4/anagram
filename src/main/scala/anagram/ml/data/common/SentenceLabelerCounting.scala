package anagram.ml.data.common

import anagram.words.WordMapper

case class SentenceLabelerCounting(lengthFactors: Map[Int, Double], wm: WordMapper) extends SentenceLabeler {

  override def labelSentence(sentences: Iterable[Sentence]): Iterable[Labeled] = {
    val s1 = sentences.map{s =>
      val w1 = s.words.flatMap(wm.transform(_))
      s.copy(words = w1)
    }
    val rmap: Seq[(Seq[String], Iterable[Sentence])] = s1.groupBy(sent => sent.words).toSeq
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
