package anagram.solve

import anagram.ml.data.WordList

import scala.collection.GenIterable

object SSolverTryout extends App {

  val wl = WordList.loadWordList("wordlist/wordlist_small.txt")

  val start = System.currentTimeMillis()

  private val src = "clint eastwood"
  val anas: GenIterable[Iterable[String]] = SSolver().solve(src, wl)

  if (anas.isEmpty) println("-- empty --")
  else for (sent <- anas) {
    val str = sent.mkString(" ")
    println("%s --> %s".format(src, str))
  }

  val stop = System.currentTimeMillis()

  println(s"Duration: ${stop - start} ms")
}
