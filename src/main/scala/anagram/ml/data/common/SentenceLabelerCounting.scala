package anagram.ml.data.common

import anagram.words.{Grouper, WordMapper}

case class SentenceLabelerCounting(lengthFactors: Map[Int, Double], wm: WordMapper, grp:Grouper ) extends SentenceLabeler {

  override def labelSentence(sentences: Seq[Sentence]): Seq[Labeled] = {
    val s1 = sentences.map { s =>
      val w1 = s.words.flatMap(grp.group)
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
            case SentenceType_BEGINNING => r + cnt * 5 * factor
            case SentenceType_OTHER => r + cnt * 10 * factor
            case SentenceType_RANDOM => throw new IllegalStateException("SentenceType_RANDOM makes no sense for Counting")
          }
        }
        Some(Labeled(Sentence(SentenceType_OTHER, w), features(w), rating))
      }
    }
  }

  def features(sentence: Seq[String]): Seq[Double] = sentence
    .flatMap(grp.group)
    .map(wm.toNum(_).toDouble)

}
