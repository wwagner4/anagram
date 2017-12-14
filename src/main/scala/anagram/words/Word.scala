package anagram.words

case class Word(word: String, wordSorted: String, grp: Option[String] = None, rating: Option[Double] = None)
