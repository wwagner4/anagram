package anagram.words

trait WordMapperPrediction {

  def transform(value: String): Seq[String]

  def toNum(word: String): Int

}
