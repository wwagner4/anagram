package anagram.ml.data.common

case class Labeled(features: Seq[Double], label: Double)

trait SentenceLabeler {

  /**
    * Takes a sentence, and returns a sequence
    * of labeled sentences.
    * The parameter sentences must be a sequence because it might
    * be necessary to label sentences according to all sentences of the testdata.
    * Example: SentenceLabelerCounting
    */
  def labelSentence(sentences: Seq[Sentence]): Seq[Labeled]

}


