package anagram.ml.data.analyze

import anagram.common.IoUtil
import anagram.ml.MlUtil

object CheckData extends App {
  
  val length = 2 to 5
  val id = "plainRated001"


  def checkLines(fn: String, len: Int)(lines: Iterator[String]): Unit = {
    println(s"-- checking $fn")
    val expLen = len * 2 + 1
    for ((line, i) <- lines.zipWithIndex) {
      val sl = line.split(";")
      if (sl.length != expLen) throw new IllegalStateException(s"ERROR A in $fn at Line $i. '$line'")
    }
  }

  for (len <- length) {
    val fn = MlUtil.dataFileName(id, "" + len)
    val file = IoUtil.dirWork.resolve(fn)
    IoUtil.loadTxtFromFile(file, checkLines(fn, len)(_))
    println(s"-- OK $fn")
  }
  println(s"-- finished")

}
