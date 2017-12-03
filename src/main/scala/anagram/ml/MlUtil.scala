package anagram.ml

object MlUtil {

  def dataFileName(id: String, sentenceLength: Int): String = {
    s"anagram_${id}_data_$sentenceLength.txt"
  }

  def nnFileName(id: String, sentenceLength: Int): String = {
    s"anagram_${id}_nn_$sentenceLength.ser"
  }

}
