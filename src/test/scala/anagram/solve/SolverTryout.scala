package anagram.solve

import anagram.common.IoUtil
import anagram.ssolve.SSolver


object SolverTryout extends App {

  val id = "en01"


  solve()

  def loadDict(id: String): Iterable[String] = {
    IoUtil.loadTxtFromWorkDir(s"${id}_dict", loadWords)
  }

  def loadWords(lines: Iterator[String]): Iterable[String] = {
    lines.toSeq
  }

  def solve(): Unit = {

    val words = loadDict(id)

    val re = SSolver.solve("bernd ingrid", words)


    val atStart = System.currentTimeMillis()
    for ((sent, i) <- re.zipWithIndex) {
      println("%7d  -  %s".format(i, sent.mkString(" ")))
    }
    val atEnd = System.currentTimeMillis()

    println(s"-- duration: ${atEnd - atStart} ms")

  }

}

