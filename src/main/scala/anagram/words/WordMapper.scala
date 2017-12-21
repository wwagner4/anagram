package anagram.words

trait WordMapper[T] extends WordMapperRating[T] {

  def size: Int

  def containsWord(str: String): Boolean

  def wordList: Iterable[Word]

}

