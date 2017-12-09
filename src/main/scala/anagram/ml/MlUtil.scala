package anagram.ml

object MlUtil {

  def dataFileName(id: String, sentId: String): String = {
    s"anagram_${id}_data_$sentId.txt"
  }

  def nnFileName(id: String, sentId: String): String = {
    s"anagram_${id}_nn_$sentId.ser"
  }

}
