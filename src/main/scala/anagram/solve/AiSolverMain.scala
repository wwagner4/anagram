package anagram.solve

import anagram.ml.data.WordList
import org.slf4j.LoggerFactory

object AiSolverMain extends App {

  val log = LoggerFactory.getLogger("anagram.solve.AiSolverMain")

  val srcTexts01 = List(
    //"elvis", // -> lives
    "clint eastwood", // -> old west action
    //"william shakespeare", // -> i am a weakish speller
    //"leornado da vinci", // -> did color in a nave
  )
  val srcTexts02 = List(
    "clint eastwood", // -> old west action
  )
  val srcTexts = srcTexts02

  val id: String = "en04"
  val wordlist: Iterable[String] = WordList.loadWordList("wordlist/wordlist_small.txt")
  val rater: Rater = new AiRater(id, wordlist)

  for (srcText <- srcTexts) {
    val anas = AiSolver.solve(srcText, wordlist, rater)

    println("------------------------")
    println(s"Anagrams for '$srcText'")
    for ((ana, i) <- anas.toList.sortBy(-_.rate).zipWithIndex) {
      println("%5d - %5.2f - '%s'".format(i + 1, ana.rate, ana.sentence.mkString(" ")))
    }
  }

}
