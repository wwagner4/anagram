package anagram.solve

import anagram.ml.data.WordList
import org.slf4j.LoggerFactory

object AiSolverMain extends App {

  val log = LoggerFactory.getLogger("anagram.solve.AiSolverMain")

  val srcText: String = "wolfgang wagner"
  val id: String = "en03"
  val wordlist: Iterable[String] = WordList.loadWordList("wordlist/wordlist_small.txt")
  val rater: Rater = new AiRater(id, wordlist)

  log.info(s"STARTED src text: $srcText")
  AiSolver.solve(srcText, id, wordlist, rater)
  log.info(s"FINISHED src text: $srcText")

}
