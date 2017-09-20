package anagram.ml.data

import anagram.common.IoUtil

object WordMapTryout extends App {

  val id = "tryout_twoLines"
  val uris = IoUtil.uris(BookSplitter.booksSmall)

  val wm = WordMap.createWordMap(uris)

  //println(is.mkString("\n"))
  println(s"-- size:${wm.size}")

  IoUtil.saveTxtToWorkDir(id, wm.writeMap)


  println("-- a")
  val map: WordMapper = IoUtil.loadTxtFromWorkDir(id, WordMap.loadMap)
  println("-- b")

  println("following -> " + map.toNum("following"))
  println("a -> " + map.toNum("a"))

}
