package anagram.ml.data

object WordMapTryout extends App {

  val id = "tryout_twoLines"

  val (is, si) = WordMap.createWordMap(BookSplitter.booksTwoLines)

  //println(is.mkString("\n"))
  println(s"-- size:${is.size}")

  IoUtil.saveTxtToWorkDir("wordmap_c", WordMap.writeMap(si)(_))


  println("-- a")
  val map: Map[String, Int] = IoUtil.loadTxtFromWorkDir("wordMap_c", WordMap.loadMap)
  println("-- b")

  println("following -> " + map("following"))
  println("a -> " + map("a"))

}
