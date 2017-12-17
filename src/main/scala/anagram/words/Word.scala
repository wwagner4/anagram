package anagram.words

/**
  *
  * @param word The word
  * @param wordSorted The word sorted by characters. Improves performance of the solver. 'anna' -> 'aann'
  * @param grp Can be the Grammar type for example
  * @param rating Can be a value indicating the frequency o a word used in common texts.
  */
case class Word(word: String, wordSorted: String, grp: Option[String] = None, rating: Option[Double] = None)
