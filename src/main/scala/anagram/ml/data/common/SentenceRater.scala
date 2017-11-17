package anagram.ml.data.common

case class Rated(sentence: Sentence, rating: Double)

trait SentenceRater {

  /**
    * Takes a sentence, and returns a sequence
    * rated sentences.
    */
  def rateSentence(sentence: Iterable[Sentence]): Iterable[Rated]

}


