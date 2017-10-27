package anagram.solve

import anagram.words.WordMappers

object SSolverTryout extends App {

  val wl =  WordMappers.createWordMapperGrammer.wordList

  val start = System.currentTimeMillis()

  private val src = "noah wagner"
  val anas = SSolver(maxDepth = 4, parallel = 4).solve(src, wl)

  if (anas.isEmpty) println("-- empty --")
  else for ((ana, i) <- anas.zipWithIndex) {
    val str = ana.sentence.mkString(" ")
    println("%10d - %s".format(i, str))
  }

  val stop = System.currentTimeMillis()

  println(s"Duration: ${stop - start} ms")

}