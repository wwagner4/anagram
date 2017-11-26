package anagram.ml.data.common

trait BookSplitter {

  def splitSentences(resName: String): Stream[Seq[String]]

}
