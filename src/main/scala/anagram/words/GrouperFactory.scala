package anagram.words

trait GrouperFactory {

  def grouper(wordList: Iterable[Word]): Grouper

}
