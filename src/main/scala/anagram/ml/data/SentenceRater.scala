package anagram.ml.data

trait SentenceRater {

  /**
    * Takes a groups, copies and changes these copies
    * and adds a rating at the end of each of these new
    * sentences.
    */
  def rateSentence(sentence: Sentence): Seq[Rated]

}

case class Rated(sentence: Sentence, rating: Double)

