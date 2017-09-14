package anagram.ml.data

import java.io.BufferedWriter

object WordMap {

  def createWordMap(books: Seq[Book]): (Map[Int, String], Map[String, Int]) = {
    val words: Seq[String] = BookSplitter.sentances(books)
      .flatten
      .toSet
      .toSeq
    val si: Seq[(String, Int)] = words.zipWithIndex
    val is = si.map { case (a, b) => (b, a) }
    (is.toMap, si.toMap)
  }

  def writeMap(intValueMap: Map[String, Int])(wr: BufferedWriter): Unit = {
    for ((s, i) <- intValueMap.iterator) {
      wr.write(s"$s $i\n")
    }
  }

  def loadMap(lines: Iterator[String]): Map[String, Int] = {

    def lineToTuple(line: String): (String, Int) = {
      val sp = line.split("\\s")
      (sp(0), sp(1).toInt)
    }

    lines.toStream.map(lineToTuple).toMap
  }

}
