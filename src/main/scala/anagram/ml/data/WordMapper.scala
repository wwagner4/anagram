package anagram.ml.data

trait WordMapper {

  def group(value: String): String

  def toNum(word: String): Int

  def toWord(num: Int): String

  def size: Int

  def randomWord: String

  def containsWord(str: String): Boolean

}

