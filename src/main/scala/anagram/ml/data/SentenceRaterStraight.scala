package anagram.ml.data

class SentenceRaterStraight(val wm: WordMapper) extends SentenceRater {

  val ran = new util.Random()

  def rateSentence(sentence: Sentence): Seq[Rated] = {

    if (!sentence.words.forall(w => wm.containsWord(w))) {
      Seq.empty[Rated]
    } else {
      Seq(Rated(sentence, rating(sentence)))
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
    if (len <= 1) 2.0
    else if (len <= 2) 1.7
    else if (len <= 3) 1.6
    else if (len <= 4) 1.5
    else if (len <= 5) 1.4
    else if (len <= 6) 1.3
    else if (len <= 7) 1.2
    else if (len <= 8) 1.1
    else 1.0
  }

}
