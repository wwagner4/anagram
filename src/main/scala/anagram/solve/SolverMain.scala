package anagram.solve

import anagram.common.IoUtil
import anagram.gui.SolverFactoryPlain
import anagram.ml.rate.RaterAi
import anagram.model.Configurations
import anagram.words.Wordlists
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object SolverMain extends App {

  implicit val exe: ExecutionContextExecutor = ExecutionContext.global

  val log = LoggerFactory.getLogger("anagram.solve.SolverMain")

  val srcTextsFull = List(
    "wolfgang",
    "ditschi",
    "ingrid bernd",
    "ditschi wolfi", //                                 12 -> 17k
    "noah the great", //                                12 -> 708k
    "clint eastwood", // -> old west action             13 -> 700k
    "leornado da vinci", // -> did color in a nave      15 -> 1900k
    "ingrid bernd in love", //                          17 ->
    "william shakespeare", // -> i am a weakish speller 18 ->
  )

  val srcTextsMedium = List(
    "wolfgang",
    "ditschi",
    "ingrid bernd",
    "ditschi wolfi", //                                 12 -> 17k
    "noah the great", //                                12 -> 708k
    "clint eastwood", // -> old west action             13 -> 700k
  )

  val srcTextsShort = List(
    "wolfgang",
    "ditschi",
    "ingrid bernd",
  )

  val srcTextsWs = List(
    "william shakespeare", // -> i am a weakish speller 18 ->
  )

  val srcTextsCe = List(
    "clint eastwood", // -> old west action 13 -> 700k
  )

  val srcTexts = srcTextsCe
  lazy val cfg = Configurations.grammarReduced.cfgRaterAi
  val idSolving = "02"

  for (srcText <- srcTexts) {
    log.info(s"Solving $srcText")

    val rater = new RaterAi(() => cfg)
    val baseSolver = SolverFactoryPlain().createSolver
    val anagrams: Iterator[Ana] = SolverRatedImpl(baseSolver, rater).solve(srcText, Wordlists.plainFreq3k)
    outWriteToFile(anagrams, srcText)
  }
  log.info("Finished")

  def outWriteToFile(anas: Iterator[Ana], srcText: String): Unit = {

    def fileName(idLearning: String, idSolving: String, src: String): String = {
      val s1 = src.replaceAll("\\s", "_")
      s"anagrams_${idLearning}_${idSolving}_$s1.txt"
    }

    val fn = fileName(cfg.id, idSolving, srcText)
    log.info(s"Write anagrams for '$srcText' to $fn")
    IoUtil.saveToWorkDir(fn, (bw) => {
      var cnt = 0
      for ((ana, i) <- anas.toList.sortBy(-_.rate).zipWithIndex) {
        if (cnt % 10000 == 0 && cnt > 0) log.info(s"Wrote $cnt anagrams")
        bw.append("%5d - %5.5f - '%s'%n".format(i + 1, ana.rate, ana.sentence.mkString(" ")))
        cnt += 1
      }
    })
  }
}
