package anagram.words

trait WordMapper[T] extends WordMapperRating[T] {

  def containsWord(str: String): Boolean

  /**
    * Maps a String to a word if it exists in the
    * word mapper
    */
  def toWord(str: String): Option[Word]

}

