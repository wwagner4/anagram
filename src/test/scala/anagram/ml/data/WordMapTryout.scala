package anagram.ml.data

object WordMapTryout extends App {

  val (is, si) = WordMap.createWordMap(BookSplitter.booksTwoLines)

  println(is.mkString("\n"))
  println(s"-- size:${is.size}")

  IoUtil.saveTxtToWorkDir("wordmap_c", WordMap.writeMap(si)(_))

}
