package anagram.words

trait WordTransformer {

  def transform(str: String): Seq[String]

  def toNum(str: String): Double

  def map(word: String): Seq[Double] = transform(word).map(toNum)

}
