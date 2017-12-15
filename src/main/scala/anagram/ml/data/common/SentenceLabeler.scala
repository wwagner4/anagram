package anagram.ml.data.common

case class Labeled(sentence: Sentence, label: Double)

trait SentenceLabeler {

  /**
    * Takes a sentence, and returns a sequence
    * of labeled sentences.
    */
  def labelSentence(sentence: Iterable[Sentence]): Iterable[Labeled]

}


