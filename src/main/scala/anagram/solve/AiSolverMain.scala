package anagram.solve

import anagram.common.IoUtil
import anagram.ml.data.{Word, WordList, WordMapGrammar}
import org.slf4j.LoggerFactory

object AiSolverMain extends App {

  val log = LoggerFactory.getLogger("anagram.solve.AiSolverMain")

  val srcTexts = List(
    "wolfgang",
    "ditschi",
    "ingrid bernd",
    "ditschi wolfi",
    "noah the great",
    //"clint eastwood", // -> old west action
    //"leornado da vinci", // -> did color in a nave
    //"william shakespeare", // -> i am a weakish speller
    //"ingrid bernd in love",
  )

  val id: String = "enGrm03"
  //val id: String = "enGrmUnrated01"
  val wordlist: Iterable[Word] = WordList.loadWordListGrammarWords
  log.info(s"wordlist (size): ${wordlist.size}")

  val wordMapper = WordMapGrammar.createWordMapperFull
  //val rater: Rater = new RaterAi(id, wordMapper)
  val rater: Rater = new RaterNone
  val baseSolver = SSolver(maxDepth = 4, parallel = 5)
  val aiSolver = AiSolver(baseSolver, rater)

  for (srcText <- srcTexts) {
    val fn = fileName(id, srcText)
    log.info(s"Write anagrams for '$srcText' to $fn")
    val anas = aiSolver.solve(srcText, wordlist)
    IoUtil.saveToWorkDir(fn, (bw) => {
      var cnt = 0
      for ((ana, i) <- anas.toList.sortBy(-_.rate).zipWithIndex) {
        if (cnt % 10000 == 0 && cnt > 0) log.info(s"Wrote $cnt anagrams")
        bw.append("%5d - %5.5f - '%s'%n".format(i + 1, ana.rate, ana.sentence.mkString(" ")))
        cnt += 1
      }
    })
  }
  log.info("Finished")

  def fileName(id: String, src: String): String = {
    val s1 = src.replaceAll("\\s", "_")
    s"${id}_$s1.txt"
  }

}
