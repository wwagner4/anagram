package anagram.solve

import java.nio.file.Paths

import anagram.common.IoUtil

object SolverTryout extends App {

  //val dict = IoUtil.getTxtFilePathFromWorkDir("en01_dict")
  val dict = Paths.get(IoUtil.uri("wordlist/wordlist.txt"))

  val re = Solver.solve("ingrid and bernd are in love", dict)

  for ((sent, i) <- re.iterator.zipWithIndex) {
    println("%7d  -  %s".format(i, sent.mkString(" ")))
  }
}
