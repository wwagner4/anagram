package anagram.words

trait WordListFactory {

  def description: String

  def shortSescription: String

  def wordList: () => Iterable[Word]

}
