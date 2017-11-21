package anagram.solve

import anagram.ml.data.datamodel.grm.WordMapperFactoryGrammer

import scala.concurrent.ExecutionContext

object SolverPlainTryout extends App {

  implicit val ec: ExecutionContext = ExecutionContext.global

  val wl =  WordMapperFactoryGrammer.create.wordList

  val start = System.currentTimeMillis()

  private val src = "noah wagner"
  val anas = SolverPlain(maxDepth = 4, parallel = 4).solve(src, wl)

  if (anas.isEmpty) println("-- empty --")
  else for ((sentence, i) <- anas.zipWithIndex) {
    val str = sentence.mkString(" ")
    println("%10d - %s".format(i, str))
  }

  val stop = System.currentTimeMillis()

  println(s"Duration: ${stop - start} ms")

}