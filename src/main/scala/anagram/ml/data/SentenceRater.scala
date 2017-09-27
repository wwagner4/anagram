package anagram.ml.data

trait SentenceRater {

  /**
    * Takes a sentence, copies and changes these copies
    * and adds a rating at the end of each of these new
    * sentences.
    */
  def rateSentence(sentence: Seq[String]): Seq[Rated]

}

case class Rated(sentence: Seq[String], rating: Double)

