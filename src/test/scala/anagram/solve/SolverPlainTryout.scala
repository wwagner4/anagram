package anagram.solve

import anagram.model.grm.WordMapperFactoryGrammar
import anagram.words.Wordlists

import scala.concurrent.ExecutionContext

object SolverPlainTryout extends App {

  implicit val ec: ExecutionContext = ExecutionContext.global

  val wl = Wordlists.plainFreq5k.wordList()

  val start = System.currentTimeMillis()

  private val src = "the old man and the sea"
  val anas = SolverPlain(maxDepth = 4, parallel = 4, wl).solve(src)

  if (anas.isEmpty) println("-- empty --")
  else for ((sentence, i) <- anas.zipWithIndex) {
    val str = sentence.mkString(" ")
    println("%10d - %s".format(i, str))
  }

  val stop = System.currentTimeMillis()

  println(s"Duration: ${stop - start} ms")

}