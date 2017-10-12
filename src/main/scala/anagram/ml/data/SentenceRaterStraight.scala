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
      case SentenceType_COMPLETE => 100.0
      case SentenceType_BEGINNING => 50.0
      case SentenceType_OTHER => 20.0
    }
  }

}
