package anagram.solve

import java.nio.file.Paths

import anagram.common.IoUtil
import anagram.gui.SolverFactoryPlain
import anagram.ml.rate.RaterAi
import anagram.model.{CfgModel, Configurations}
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
    "scala user group",
    "leornado da vinci", // -> did color in a nave      15 -> 1900k
    "ingrid bernd in love", //                          17 ->
    "william shakespeare", // -> i am a weakish speller 18 ->
    "some nice text",
    "another nice text",
    "a not so nice text",
    "a very bad text",
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

  val srcTextsTmp = List(
    "old man at home",
  )

  val srcTexts = srcTextsShort
  val idSolving = "08"
  val wlf = Wordlists.plainFreq30k

  for (cfg <- Configurations.all.par) {
    for (srcText <- srcTexts) {
      log.info(s"Solving $srcText")

      val rater = new RaterAi(cfg.cfgRaterAi.cfgRaterAi())
      val baseSolver = SolverFactoryPlain(maxDepth = 5, parallel = 4, wlf).createSolver
      val anagrams: Iterator[Ana] = SolverRatedImpl(baseSolver, rater).solve(srcText)
      outWriteToFile(anagrams, srcText, cfg)
    }
  }

  log.info("Finished")

  def outWriteToFile(anas: Iterator[Ana], srcText: String, cfg: CfgModel): Unit = {

    def fileName(idLearning: String, src: String): String = {
      val s1 = src.replaceAll("\\s", "_")
      s"ana_${idLearning}_$s1.txt"
    }

    val fn = fileName(cfg.cfgRaterAi.cfgRaterAi().id, srcText)
    val dir = IoUtil.dirOut.resolve(Paths.get(idSolving, cfg.cfgRaterAi.shortDescription, wlf.shortSescription))
    IoUtil.save(dir, fn, (bw) => {
      var cnt = 0
      for ((ana, i) <- anas.toList.sortBy(-_.rate).zipWithIndex) {
        if (cnt % 10000 == 0 && cnt > 0) log.info(s"Wrote $cnt anagrams")
        bw.append("%5d - %5.5f - '%s'%n".format(i + 1, ana.rate, ana.sentence.mkString(" ")))
        cnt += 1
      }
    })
    log.info(s"Wrote anagrams for '$srcText' to $fn")
  }
}
