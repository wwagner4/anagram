package anagram.ml.data.common

import anagram.words.WordMapper

/**
  * Rates all sentences containing words depending on their
  * position in real sentences (BEGINNING or OTHER) and weather
  * they exist as real sentences (COMPLETE) or not.
  */
class SentenceRaterStraight(val wm: WordMapper) extends SentenceRater {

  val ran = new util.Random()

  def rateSentence(sentences: Iterable[Sentence]): Iterable[Rated] = {
    sentences.flatMap { sentence =>
      if (!sentence.words.forall(w => wm.containsWord(w))) None
      else Some(Rated(sentence, rating(sentence)))
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
