package anagram.solve

import java.nio.file.Paths

import anagram.common.IoUtil

object SolverTryout extends App {

  val dict1 = IoUtil.getTxtFilePathFromWorkDir("en01_dict")
  val dict2 = Paths.get(IoUtil.uri("wordlist/wordlist.txt"))

  val re = Solver.solve("bernd lives with ingrid in vienna", dict1)


  val atStart = System.currentTimeMillis()
  for ((sent, i) <- re.zipWithIndex) {
    println("%7d  -  %s".format(i, sent.mkString(" ")))
  }
  val atEnd = System.currentTimeMillis()

  println(s"-- duration: ${atEnd - atStart} ms")
}
