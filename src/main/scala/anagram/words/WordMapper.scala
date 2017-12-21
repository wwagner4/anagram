package anagram.words

trait WordMapper extends WordMapperRating {

  def size: Int

  def containsWord(str: String): Boolean

  def wordList: Iterable[Word]

  def toNum(word: String): Int

}

