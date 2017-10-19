package anagram.solve

import anagram.ml.data.WordList

object SSolverTryout extends App {

  val wl =  WordList.loadWordListSmall

  val start = System.currentTimeMillis()

  private val src = "ingrid in love bernd"
  val anas = SSolver(4).solve(src, wl)

  if (anas.isEmpty) println("-- empty --")
  else for ((ana, i) <- anas.zipWithIndex) {
    val str = ana.sentence.mkString(" ")
    println("%10d - %s".format(i, str))
  }

  val stop = System.currentTimeMillis()

  println(s"Duration: ${stop - start} ms")

}
