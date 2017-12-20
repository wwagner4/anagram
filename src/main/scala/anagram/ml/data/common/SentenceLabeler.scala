package anagram.ml.data.common

case class Labeled(sentence: Sentence, features: Seq[Double], label: Double)

trait SentenceLabeler {

  /**
    * Takes a sentence, and returns a sequence
    * of labeled sentences.
    */
  def labelSentence(sentence: Seq[Sentence]): Seq[Labeled]

}


