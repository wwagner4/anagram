package anagram.ml

object MlUtil {

  def dataFileName(id: String, sentenceLength: Int, additionalId: Option[String]): String = {
    val ai = additionalId.map(id => s"_$id").getOrElse("")
    s"anagram_$id${ai}_data_$sentenceLength.txt"
  }

  def nnFileName(id: String, sentenceLength: Int): String = {
    s"anagram_${id}_nn_$sentenceLength.ser"
  }

}
