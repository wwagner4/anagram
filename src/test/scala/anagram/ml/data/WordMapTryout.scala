package anagram.ml.data

object WordMapTryout extends App {

  val (is, si) = WordMap.createWordMap(BookSplitter.books)

  println(is.mkString("\n"))
  println(s"-- size:${is.size}")

  WordMap.save("wordmap_b.txt", WordMap.writeMap(si)(_))

}
