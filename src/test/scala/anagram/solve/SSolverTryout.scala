package anagram.solve

import java.nio.file.Paths

import anagram.common.IoUtil
import anagram.ml.data.WordList

object SSolverTryout extends App {

  val wordlist = List(
    "as",
    "togo",
    "go",
    "r",
  )

  val wl = WordList.loadWordList(Paths.get(IoUtil.uri("wordlist/wordlist_test01.txt")))
  val anas = SSolver.solve("wolfgang wagner", wl)

  if (anas.isEmpty) println("-- empty --")
  else for ((sent, i) <- anas.zipWithIndex) {
    val str = sent.mkString(" ")
    println(f"$i%10d - '$str'")
  }
}
