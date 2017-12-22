package anagram.ml.data.common

import anagram.words.WordMapper

/**
  * Rates all sentences containing words depending on their
  * position in real sentences (BEGINNING or OTHER) and weather
  * they exist as real sentences (COMPLETE) or not.
  */
class SentenceLabelerStraight(val wm: WordMapper[Seq[String]]) extends SentenceLabeler {

  val ran = new util.Random()

  def labelSentence(sentences: Seq[Sentence]): Seq[Labeled] = {
    sentences.flatMap { sentence =>
      if (sentence.words.forall(w => wm.toWord(w).isDefined)) {
        val mr = wm.map(sentence.words)
        Some(Labeled(mr.features, rating(sentence)))
      }
      else None
    }
  }

  def rating(sentence: Sentence): Double = {
    sentence.sentenceType match {
      case SentenceType_COMPLETE => 100.0
      case SentenceType_BEGINNING => 60.0
      case SentenceType_OTHER => 30.0
      case SentenceType_RANDOM => 10.0
    }
  }

}
