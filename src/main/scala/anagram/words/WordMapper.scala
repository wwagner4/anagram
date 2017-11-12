package anagram.words

trait WordMapper {

  def group(value: String): Seq[String]

  def toNum(word: String): Int

  def toWord(num: Int): String

  def size: Int

  def randomWord: String

  def containsWord(str: String): Boolean

  def wordList: Iterable[Word]

}

