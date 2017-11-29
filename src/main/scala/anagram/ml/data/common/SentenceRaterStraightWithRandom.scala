package anagram.ml.data.common

import anagram.words.WordMapper

/**
  * Rates all sentences like SentenceRaterStraight but adds one
  * random zero rated sentence for every 'normal' sentence.
  */
class SentenceRaterStraightWithRandom(val wm: WordMapper) extends SentenceRater {

  val ran = new util.Random()

  def randomSentence(length: Int): Sentence =
    Sentence(
      SentenceType_RANDOM,
      (1 to length).map(_ => wm.randomWord),
    )

  def rateSentence(sentences: Iterable[Sentence]): Iterable[Rated] = {
    sentences.flatMap { sentence =>
      if (!sentence.words.forall(w => wm.containsWord(w))) {
        Seq.empty[Rated]
      } else {
        val ranSent = randomSentence(sentence.words.size)
        Seq(
          Rated(sentence, rating(sentence)),
          Rated(ranSent, rating(ranSent)),
        )
      }
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
