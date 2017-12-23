package anagram.model.plainrated

import anagram.ml.data.common._
import anagram.words.{MappingResult, WordMapper}

class SentenceLabelerPlainRated(wm: WordMapper[Seq[Double]]) extends SentenceLabeler {

  /**
    * Takes a sentence, and returns a sequence
    * of labeled sentences.
    */
  override def labelSentence(sentence: Seq[Sentence]): Seq[Labeled] = {
    sentence.map { s =>
      val mr = wm.map(s.words)
      Labeled(
        mr.features,
        label(s, mr),
      )
    }
  }

  def sent(sentenceType: SentenceType): Double = sentenceType match {
    case SentenceType_COMPLETE => 100
    case SentenceType_BEGINNING => 50
    case SentenceType_OTHER => 10
    case SentenceType_RANDOM => throw new IllegalStateException("RANDOM makes here no senese")
  }

  def label(s: Sentence, mr: MappingResult[Seq[Double]]): Double = {
    val _sum = mr.intermediate.sum
    val _sent = sent(s.sentenceType)
    val r = _sum + _sent
    println(s"--- label sum:${_sum} sent:${_sent} r:$r")
    r
  }
}
