package anagram.words

trait WordMapperRating {

  def transform(value: String): Seq[String]

  def toNum(word: String): Int

}
