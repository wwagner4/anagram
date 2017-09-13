package anagram.ml.data

import java.nio.file.{Files, Path, Paths}

object WordMap {

  def createWordMap(books: Seq[Book]): (Map[Int, String], Map[String, Int]) = {
    val words: Seq[String] = BookSplitter.sentances(books)
      .flatten
      .toSet
      .toSeq
    val si: Seq[(String, Int)] = words.zipWithIndex
    val is = si.map{case (a, b) => (b, a)}
    (is.toMap, si.toMap)
  }

  def getCreateWorkDir(): Path = {
    val dirWork: Path = Paths.get(System.getProperty("user.home"), "anagram", "work")
    if (!Files.exists(dirWork)) {
      Files.createDirectories(dirWork)
    }
    dirWork
  }

  def save(id: String, intValueMap: Map[String, Int]): Unit = {
    val dirWork: Path = getCreateWorkDir()
//    dirWork.
//    Files.newBufferedWriter()
    ???
  }

}
