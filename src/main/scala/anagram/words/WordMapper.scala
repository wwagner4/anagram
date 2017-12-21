package anagram.words

trait WordMapper[T] extends WordMapperRating[T] {

  def containsWord(str: String): Boolean

}

