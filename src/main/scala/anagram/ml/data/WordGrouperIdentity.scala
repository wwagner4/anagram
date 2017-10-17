package anagram.ml.data

object WordGrouperIdentity extends WordGrouper {

  override def group(word: String) = word

}
