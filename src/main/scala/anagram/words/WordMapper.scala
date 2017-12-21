package anagram.words

trait WordMapper extends WordMapperRating {

  def size: Int

  def randomWord: String

  def containsWord(str: String): Boolean

  def wordList: Iterable[Word]

  def toNum(word: String): Int

}

