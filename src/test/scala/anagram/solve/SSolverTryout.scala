package anagram.solve

import anagram.ml.data.WordList

object SSolverTryout extends App {

  val wl = WordList.loadWordList("wordlist/wordlist_small.txt")

  val start = System.currentTimeMillis()

  private val src = "clint eastwood"
  val anas = SSolver(4).solve(src, wl)

  if (anas.isEmpty) println("-- empty --")
  else for (ana <- anas) {
    val str = ana.sentence.mkString(" ")
    println("%s --> %s".format(src, str))
  }

  val stop = System.currentTimeMillis()

  println(s"Duration: ${stop - start} ms")
}
