package anagram.solve

import java.nio.file.Files

import anagram.common.IoUtil

object SolverTryout extends App {

  val dict = IoUtil.getTxtFilePathFromWorkDir("en01_dict")
  //val dict = Paths.get(IoUtil.uri("wordlist/wordlist.txt"))

  println("dict exist: " + Files.exists(dict))

  val re = Solver.solve("ingrid bernd love", dict)

  println("re size:" + re.size)
  println(re.take(100).map(_.mkString(" ")).mkString("\n"))

}
