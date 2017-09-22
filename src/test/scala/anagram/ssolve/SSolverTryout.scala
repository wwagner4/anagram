package anagram.ssolve

import anagram.common.IoUtil
import anagram.ml.data.WordMap

object SSolverTryout extends App {

  val wordlist = List(
    "as",
    "togo",
    "go",
    "r",
  )

  val anas = SSolver.solve("oastogr", wordlist)

  if (anas.isEmpty) println("-- empty --")
  else println(anas.map(l => s"""'${l.mkString(" ")}'""").mkString("\n"))

}
