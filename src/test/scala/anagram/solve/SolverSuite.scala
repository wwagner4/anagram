package anagram.solve

import anagram.gui.SolverFactoryPlain
import anagram.ml.rate.RaterAi
import anagram.model.Configurations
import anagram.words.Wordlists
import org.scalatest.{FunSuite, MustMatchers}
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

class SolverSuite extends FunSuite with MustMatchers {

  implicit val exe: ExecutionContextExecutor = ExecutionContext.global

  private val log = LoggerFactory.getLogger("anagram.solve.SolverMain")


  test("solving and rating") {
    val srcTextsShort = List(
      "wolfgang",
      "ditschi",
      "ingrid bernd"
    )

    val srcTexts = srcTextsShort
    val wlf = Wordlists.plainFreq30k

    val expected = Seq(
      ("PLAIN_plain001", 32.9957, List("flag", "gown")),
      ("PLAIN_plain001", 33.4897, List("gown", "flag")),
      ("PLAIN_plain001", 33.589, List("is", "ditch")),
      ("PLAIN_plain001", 33.6183, List("hit", "disc")),
      ("PLAIN_plain001", 32.6135, List("in", "in", "beg", "dr", "dr")),
      ("PLAIN_plain001", 32.6135, List("in", "in", "beg", "rr", "dd")),
      ("PLAINR_plainRand001", 22.10758, List("flag", "gown")),
      ("PLAINR_plainRand001", 22.50175, List("gown", "flag")),
      ("PLAINR_plainRand001", 22.657, List("is", "ditch")),
      ("PLAINR_plainRand001", 22.65767, List("hit", "disc")),
      ("PLAINR_plainRand001", 21.4158, List("in", "in", "beg", "dr", "dr")),
      ("PLAINR_plainRand001", 21.4158, List("in", "in", "beg", "rr", "dd")),
      ("GRM_grm001", 2.6653, List("flag", "gown")),
      ("GRM_grm001", 2.608, List("gown", "flag")),
      ("GRM_grm001", 2.6127, List("is", "ditch")),
      ("GRM_grm001", 2.5975, List("hit", "disc")),
      ("GRM_grm001", 2.4577, List("in", "in", "beg", "dr", "dr")),
      ("GRM_grm001", 2.4577, List("in", "in", "beg", "rr", "dd")),
      ("GRMRED_grmRed001", 14.963, List("flag", "gown")),
      ("GRMRED_grmRed001", 14.975, List("gown", "flag")),
      ("GRMRED_grmRed001", 15.014, List("is", "ditch")),
      ("GRMRED_grmRed001", 14.882, List("hit", "disc")),
      ("GRMRED_grmRed001", 14.962, List("in", "in", "beg", "dr", "dr")),
      ("GRMRED_grmRed001", 14.962, List("in", "in", "beg", "rr", "dd"))
    )

    var i = 0
    for (cfg <- Configurations.all) {
      for (srcText <- srcTexts) {
        log.info(s"Solving $srcText")

        val rater = new RaterAi(cfg.cfgRaterAi.cfgRaterAi())
        val baseSolver = SolverFactoryPlain(maxDepth = 5, parallel = 4, wlf).createSolver
        val anagrams: Iterator[Ana] = SolverRatedImpl(baseSolver, rater).solve(srcText)
        for (ana <- anagrams.take(2)) {
          val rdesc = cfg.cfgRaterAi.shortDescription
          val (expectedRater, expectedRating, expectedSent) = expected(i)
          rdesc mustBe expectedRater
          ana.rate mustBe expectedRating +- 1.0
          ana.sentence mustBe expectedSent
          i += 1
        }
      }
    }
  }
}
