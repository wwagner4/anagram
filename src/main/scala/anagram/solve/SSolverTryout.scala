package anagram.solve

import anagram.words.WordMappers

import scala.concurrent.ExecutionContext

object SSolverTryout extends App {

  implicit val ec: ExecutionContext = ExecutionContext.global

  val wl =  WordMappers.createWordMapperGrammer.wordList

  val start = System.currentTimeMillis()

  private val src = "noah wagner"
  val anas = SolverPlain(maxDepth = 4, parallel = 4).solve(src, wl)

  if (anas.isEmpty) println("-- empty --")
  else for ((ana, i) <- anas.zipWithIndex) {
    val str = ana.sentence.mkString(" ")
    println("%10d - %s".format(i, str))
  }

  val stop = System.currentTimeMillis()

  println(s"Duration: ${stop - start} ms")

}