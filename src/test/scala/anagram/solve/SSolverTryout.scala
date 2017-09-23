package anagram.solve

import anagram.ml.data.WordList

object SSolverTryout extends App {

  val wordlist = List(
    "as",
    "togo",
    "go",
    "r",
  )

  val wl = WordList.loadWordListFromWorkdir("test")
  val anas = SSolver.solve("wolfi", wl)

  if (anas.isEmpty) println("-- empty --")
  else for ((sent, i) <- anas.zipWithIndex) {
    val str = sent.mkString(" ")
    println(f"$i%10d - '$str'")
  }
}
