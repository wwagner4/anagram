package anagram.model.plainrated

import anagram.ml.data.common._
import anagram.words.{MappingResult, WordMapper}

class SentenceLabelerPlainRated(wm: WordMapper[Seq[Double]]) extends SentenceLabeler {

  /**
    * Takes a sentence, and returns a sequence
    * of labeled sentences.
    */
  override def labelSentence(sentences: Seq[Sentence]): Seq[Labeled] = {
    sentences.flatMap { s =>
      // Filter sentences containing word that are not in the word mapper
      if (s.words.forall(w => wm.toWord(w).isDefined)) {
        val mr = wm.map(s.words)
        Some(Labeled(mr.features, label(s, mr)))
      } else {
        None
      }
    }
  }

  def sent(sentenceType: SentenceType, sentLen: Int): Double = sentenceType match {
    case SentenceType_COMPLETE => 10 * sentLen
    case SentenceType_BEGINNING => 5 * sentLen
    case SentenceType_OTHER => 1 * sentLen
    case SentenceType_RANDOM => throw new IllegalStateException("RANDOM makes here no senese")
  }

  def label(s: Sentence, mr: MappingResult[Seq[Double]]): Double = {
    val _sum = mr.intermediate.map(_ / 500.0).sum
    val _sent = sent(s.sentenceType, s.words.size)
    _sum + _sent
  }
}
