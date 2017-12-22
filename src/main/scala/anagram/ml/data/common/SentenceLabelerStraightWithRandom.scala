package anagram.ml.data.common

import anagram.words.{WordMapper, WordRandom}

/**
  * Rates all sentences like SentenceRaterStraight but adds one
  * random zero rated sentence for every 'normal' sentence.
  */
class SentenceLabelerStraightWithRandom(val wm: WordMapper[_], wr: WordRandom) extends SentenceLabeler {

  val ran = new util.Random()

  def randomSentence(length: Int): Sentence =
    Sentence(
      SentenceType_RANDOM,
      (1 to length).map(_ => wr.random),
    )

  def labelSentence(sentences: Seq[Sentence]): Seq[Labeled] = {
    sentences.flatMap { sent =>
      if (sent.words.forall(w => wm.toWord(w).isDefined)) {
        val mr = wm.map(sent.words)
        val ranSent = randomSentence(sent.words.size)
        val ranMr = wm.map(ranSent.words)
        Seq(
          Labeled(mr.features, rating(sent)),
          Labeled(ranMr.features, rating(ranSent))
        )
      } else {
        Seq.empty[Labeled]
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
