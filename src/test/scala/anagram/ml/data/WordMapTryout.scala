package anagram.ml.data

object WordMapTryout extends App {

  val id = "tryout_twoLines"
  val uris = IoUtil.uris(BookSplitter.booksSmall)

  val (is, si) = WordMap.createWordMap(uris)

  //println(is.mkString("\n"))
  println(s"-- size:${is.size}")

  IoUtil.saveTxtToWorkDir(id, WordMap.writeMap(si)(_))


  println("-- a")
  val map: Map[String, Int] = IoUtil.loadTxtFromWorkDir(id, WordMap.loadMap)
  println("-- b")

  println("following -> " + map("following"))
  println("a -> " + map("a"))

}
