package anagram.solve

import anagram.common.IoUtil
import anagram.ml.data.{Word, WordList}
import org.slf4j.LoggerFactory

object AiSolverMain extends App {

  val log = LoggerFactory.getLogger("anagram.solve.AiSolverMain")

  val srcTexts = List(
    "elvis", // -> lives
    "clint eastwood", // -> old west action
    //"william shakespeare", // -> i am a weakish speller
    //"leornado da vinci", // -> did color in a nave
    "wolfgang",
    "ditschi",
    "ditschi wolfi",
    "noah the great",
    "ingrid bernd",
  )

  val id: String = "en04"
  val wordlist: Iterable[Word] =  WordList.loadWordListSmall
  val rater: Rater = new AiRater(id, wordlist)
  val baseSolver = SSolver(4)
  val aiSolver = AiSolver(baseSolver, rater)

  for (srcText <- srcTexts) {
    val fn = fileName(id, srcText)
    log.info(s"Write anagrams for '$srcText' to $fn")
    val anas = aiSolver.solve(srcText, wordlist)
    IoUtil.saveToWorkDir(fn, (bw) => {
      var cnt = 0
      for ((ana, i) <- anas.toList.sortBy(-_.rate).zipWithIndex) {
        if (cnt % 100000 == 0) log.info(s"Wrote $cnt anagrams")
        bw.append("%5d - %5.5f - '%s'%n".format(i + 1, ana.rate, ana.sentence.mkString(" ")))
        cnt += 1
      }
    })

  }

  def fileName(id: String, src: String): String = {
    val s1 = src.replaceAll("\\s", "_")
    s"${id}_$s1.txt"
  }

}
