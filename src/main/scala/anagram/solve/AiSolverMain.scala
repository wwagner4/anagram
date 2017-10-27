package anagram.solve

import anagram.common.IoUtil
import anagram.ml.data.{Word, WordList, WordMapGrammar}
import org.slf4j.LoggerFactory

object AiSolverMain extends App {

  val log = LoggerFactory.getLogger("anagram.solve.AiSolverMain")

  val srcTextsFull = List(
    "wolfgang",
    "ditschi",
    "ingrid bernd",
    "ditschi wolfi",
    "noah the great",
    "clint eastwood", // -> old west action
    "leornado da vinci", // -> did color in a nave
    "william shakespeare", // -> i am a weakish speller
    "ingrid bernd in love",
  )

  val srcTextsShort = List(
    "wolfgang",
    "ditschi",
    "ingrid bernd",
  )

  val srcTexts = srcTextsFull

  val idLearning: String = "enGrm03"
  val idSolving: String = "01"

  val wordlist = loadWordlist

  log.info(s"wordlist (size): ${wordlist.size}")

  val wordMapper = WordMapGrammar.createWordMapperSmall
  val rater: Rater = new RaterAi(idLearning, wordMapper)
  //val rater: Rater = new RaterNone
  val baseSolver = SSolver(maxDepth = 4, parallel = 5)
  val aiSolver = AiSolver(baseSolver, rater)

  for (srcText <- srcTexts) {
    val fn = fileName(idLearning, idSolving, srcText)
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

  def fileName(idLearning: String, idSolving: String, src: String): String = {
    val s1 = src.replaceAll("\\s", "_")
    s"anagrams_${idLearning}_${idSolving}_$s1.txt"
  }

  def loadWordlist: Iterable[Word] = {
    val wordListShort = WordList.loadWordListSmall.toSet
    WordList
      .loadWordListGrammarWords
      .filter(wordListShort.contains)
  }

}
