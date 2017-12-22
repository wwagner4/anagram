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

  val srcTextsShort = List(
    "wolfgang",
    "ditschi",
    "ingrid bernd"
  )

  val expectedPlain = Seq(
    ("PLAIN_plain001", 32.9957, List("flag", "gown")),
    ("PLAIN_plain001", 33.4897, List("gown", "flag")),
    ("PLAIN_plain001", 33.589, List("is", "ditch")),
    ("PLAIN_plain001", 33.6183, List("hit", "disc")),
    ("PLAIN_plain001", 32.6135, List("in", "in", "beg", "dr", "dr")),
    ("PLAIN_plain001", 32.6135, List("in", "in", "beg", "rr", "dd")),
  )

  val expectedPlainRand = Seq(
    ("PLAINR_plainRand001", 22.10758, List("flag", "gown")),
    ("PLAINR_plainRand001", 22.50175, List("gown", "flag")),
    ("PLAINR_plainRand001", 22.657, List("is", "ditch")),
    ("PLAINR_plainRand001", 22.65767, List("hit", "disc")),
    ("PLAINR_plainRand001", 21.4158, List("in", "in", "beg", "dr", "dr")),
    ("PLAINR_plainRand001", 21.4158, List("in", "in", "beg", "rr", "dd")),
  )

  val expectedGrm = Seq(
    ("GRM_grm001", 0.56, List("flag", "gown")),
    ("GRM_grm001", 0.52, List("gown", "flag")),
    ("GRM_grm001", 0.50, List("is", "ditch")),
    ("GRM_grm001", 0.49, List("hit", "disc")),
    ("GRM_grm001", 0.29, List("in", "in", "beg", "dr", "dr")),
    ("GRM_grm001", 0.29, List("in", "in", "beg", "rr", "dd")),
  )

  val expectedGrmRed = Seq(
    ("GRMRED_grmRed001", 1.82, List("flag", "gown")),
    ("GRMRED_grmRed001", 1.80, List("gown", "flag")),
    ("GRMRED_grmRed001", 1.82, List("is", "ditch")),
    ("GRMRED_grmRed001", 1.80, List("hit", "disc")),
    ("GRMRED_grmRed001", 1.71, List("in", "in", "beg", "dr", "dr")),
    ("GRMRED_grmRed001", 1.71, List("in", "in", "beg", "rr", "dd"))
  )


  test("solving and rating plain") {
    val srcTexts = srcTextsShort
    val wlf = Wordlists.plainFreq30k

    var i = 0
    val cfg = Configurations.plain
    for (srcText <- srcTexts) {
      log.info(s"Solving $srcText")

      val rater = new RaterAi(cfg.cfgRaterAi.cfgRaterAi())
      val baseSolver = SolverFactoryPlain(maxDepth = 5, parallel = 4, wlf).createSolver
      val anagrams: Iterator[Ana] = SolverRatedImpl(baseSolver, rater).solve(srcText)
      for (ana <- anagrams.take(2)) {
        val rdesc = cfg.cfgRaterAi.shortDescription
        val (expectedRater, expectedRating, expectedSent) = expectedPlain(i)
        rdesc mustBe expectedRater
        ana.rate mustBe expectedRating +- 1.0
        ana.sentence mustBe expectedSent
        i += 1
      }
    }
  }

  test("solving and rating plain random") {
    val srcTexts = srcTextsShort
    val wlf = Wordlists.plainFreq30k

    var i = 0
    val cfg = Configurations.plainRandom
    for (srcText <- srcTexts) {
      log.info(s"Solving $srcText")

      val rater = new RaterAi(cfg.cfgRaterAi.cfgRaterAi())
      val baseSolver = SolverFactoryPlain(maxDepth = 5, parallel = 4, wlf).createSolver
      val anagrams: Iterator[Ana] = SolverRatedImpl(baseSolver, rater).solve(srcText)
      for (ana <- anagrams.take(2)) {
        val rdesc = cfg.cfgRaterAi.shortDescription
        val (expectedRater, expectedRating, expectedSent) = expectedPlainRand(i)
        rdesc mustBe expectedRater
        ana.rate mustBe expectedRating +- 1.0
        ana.sentence mustBe expectedSent
        i += 1
      }
    }
  }

  test("solving and rating grm") {
    val srcTexts = srcTextsShort
    val wlf = Wordlists.plainFreq30k

    var i = 0
    val cfg = Configurations.grammar
    for (srcText <- srcTexts) {
      log.info(s"Solving $srcText")

      val rater = new RaterAi(cfg.cfgRaterAi.cfgRaterAi())
      val baseSolver = SolverFactoryPlain(maxDepth = 5, parallel = 4, wlf).createSolver
      val anagrams: Iterator[Ana] = SolverRatedImpl(baseSolver, rater).solve(srcText)
      for (ana <- anagrams.take(2)) {
        val rdesc = cfg.cfgRaterAi.shortDescription
        val (expectedRater, expectedRating, expectedSent) = expectedGrm(i)
        rdesc mustBe expectedRater
        ana.rate mustBe expectedRating +- 1.0
        ana.sentence mustBe expectedSent
        i += 1
      }
    }
  }

  test("solving and rating grm red") {
    val srcTexts = srcTextsShort
    val wlf = Wordlists.plainFreq30k

    var i = 0
    val cfg = Configurations.grammarReduced
    for (srcText <- srcTexts) {
      log.info(s"Solving $srcText")

      val rater = new RaterAi(cfg.cfgRaterAi.cfgRaterAi())
      val baseSolver = SolverFactoryPlain(maxDepth = 5, parallel = 4, wlf).createSolver
      val anagrams: Iterator[Ana] = SolverRatedImpl(baseSolver, rater).solve(srcText)
      for (ana <- anagrams.take(2)) {
        val rdesc = cfg.cfgRaterAi.shortDescription
        val (expectedRater, expectedRating, expectedSent) = expectedGrmRed(i)
        rdesc mustBe expectedRater
        ana.rate mustBe expectedRating +- 1.0
        ana.sentence mustBe expectedSent
        i += 1
      }
    }
  }

}
