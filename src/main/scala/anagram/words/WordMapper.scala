package anagram.words

trait WordMapper extends WordMapperPrediction {

  def toWord(num: Int): String

  def size: Int

  def randomWord: String

  def containsWord(str: String): Boolean

  def wordList: Iterable[Word]


}

