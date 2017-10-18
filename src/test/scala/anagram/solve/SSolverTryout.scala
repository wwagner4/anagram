package anagram.solve

import anagram.ml.data.WordList

object SSolverTryout extends App {

  val wl =  WordList.loadWordListSmall

  val start = System.currentTimeMillis()

  private val src = "wolfi wagner"
  val anas = SSolver(3).solve(src, wl)

  if (anas.isEmpty) println("-- empty --")
  else for (ana <- anas) {
    val str = ana.sentence.mkString(" ")
    println("%s --> %s".format(src, str))
  }

  val stop = System.currentTimeMillis()

  println(s"Duration: ${stop - start} ms")

}
