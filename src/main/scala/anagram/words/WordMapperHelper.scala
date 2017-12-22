package anagram.words

object WordMapperHelper {

  /**
    * Creates a word map out of a word list
    */
  def toWordMap(wordList: Iterable[Word]): Map[String, Word] = {
    val s1: Seq[(String, Iterable[Word])] = wordList.groupBy(_.word).toSeq
    val s2: Seq[(String, Word)] = for ((k, v) <- s1) yield (k, v.toSeq(0))
    s2.toMap
  }



}
