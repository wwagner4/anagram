package anagram.ml.data

import anagram.words.WordMapper

class SentenceRaterStraight(val wm: WordMapper) extends SentenceRater {

  val ran = new util.Random()

  def rateSentence(sentences: Iterable[Sentence]): Iterable[Rated] = {
    sentences.flatMap { sentence =>
      if (!sentence.words.forall(w => wm.containsWord(w))) {
        Seq.empty[Rated]
      } else {
        Seq(Rated(sentence, rating(sentence)))
      }
    }
  }

  def rating(sentence: Sentence): Double = {
    sentence.sentenceType match {
      case SentenceType_COMPLETE => 100.0 * lengthFactor(sentence.words.size)
      case SentenceType_BEGINNING => 50.0 * lengthFactor(sentence.words.size)
      case SentenceType_OTHER => 20.0 * lengthFactor(sentence.words.size)
    }
  }

  def lengthFactor(len: Int): Double = {
    if (len <= 1) 0.45
    else if (len <= 2) 0.4672
    else if (len <= 3) 0.4521
    else if (len <= 4) 0.4474
    else if (len <= 5) 0.4483
    else if (len <= 6) 0.4451
    else if (len <= 7) 0.4421
    else 0.45
  }

}
