package anagram.words

case class Word(word: String, wordSorted: String, grp: Option[String] = None, frequency: Option[Int] = None)
