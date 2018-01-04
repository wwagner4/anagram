package anagram.words

trait WordMapperFactory[T] {

  def create: WordMapper[T]

}
