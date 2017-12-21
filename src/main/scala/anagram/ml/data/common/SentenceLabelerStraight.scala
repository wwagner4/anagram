package anagram.ml.data.common

import anagram.words.WordMapper

/**
  * Rates all sentences containing words depending on their
  * position in real sentences (BEGINNING or OTHER) and weather
  * they exist as real sentences (COMPLETE) or not.
  */
class SentenceLabelerStraight(val wm: WordMapper) extends SentenceLabeler {

  val ran = new util.Random()

  def labelSentence(sentences: Seq[Sentence]): Seq[Labeled] = {
    sentences.flatMap { sentence =>
      if (!sentence.words.forall(w => wm.containsWord(w))) None
      else Some(Labeled(sentence, features(sentence.words), rating(sentence)))
    }
  }

  def features(sentence: Seq[String]): Seq[Double] = sentence
    .map(wm.toNum(_).toDouble)

  def rating(sentence: Sentence): Double = {
    sentence.sentenceType match {
      case SentenceType_COMPLETE => 100.0
      case SentenceType_BEGINNING => 60.0
      case SentenceType_OTHER => 30.0
      case SentenceType_RANDOM => 10.0
    }
  }

}
