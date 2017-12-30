package anagram.solve

import anagram.gui.SolverFactoryPlain
import anagram.ml.rate.RaterAi
import anagram.model.Configurations
import anagram.words.Wordlists
import org.scalatest.{FunSuite, MustMatchers}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

class SolverSuite1 extends FunSuite with MustMatchers {

  case class RE(rating: Double, words: Seq[String])

  private implicit val exe: ExecutionContextExecutor = ExecutionContext.global

  private val wlf = Wordlists.plainRatedLargeFine
  private val baseSolver = SolverFactoryPlain(maxDepth = 5, parallel = 4, wlf).createSolver


  private val expectedPlain = Seq(
    ("ingrid bernd", Seq(
      RE(31.630, List("in", "bred", "grind")),
      RE(32.336, List("in", "grind", "bred")))),
  )

  for ((srcText, expSeq) <- expectedPlain) {
    val cfg = Configurations.plain

    val rdesc = cfg.cfgRaterAi.shortDescription
    val desc = expSeq.flatMap(re => re.words).mkString(" ")
    val rater = new RaterAi(cfg.cfgRaterAi.cfgRaterAi())
    val anagrams: Seq[Ana] = SolverRatedImpl(baseSolver, rater).solve(srcText).toSeq
    for (i <- 0 to 1) {
      ignore(s"$rdesc $srcText $i $desc") {
        val re = expSeq(i)
        val ana = anagrams(i)
        ana.sentence mustBe re.words
        ana.rate mustBe re.rating +- 0.1
      }
    }
  }

  val expectedPlainRand = Seq(
    ("ingrid bernd", Seq(
      RE(21.4158, List("in", "bred", "grind")),
      RE(21.4158, List("in", "grind", "bred")))),
  )

  for ((srcText, expSeq) <- expectedPlainRand) {
    val cfg = Configurations.plainRandom

    val rdesc = cfg.cfgRaterAi.shortDescription
    val desc = expSeq.flatMap(re => re.words).mkString(" ")
    val rater = new RaterAi(cfg.cfgRaterAi.cfgRaterAi())
    val anagrams: Seq[Ana] = SolverRatedImpl(baseSolver, rater).solve(srcText).toSeq
    for (i <- 0 to 1) {
      ignore(s"$rdesc $srcText $i $desc") {
        val re = expSeq(i)
        val ana = anagrams(i)
        ana.sentence mustBe re.words
        ana.rate mustBe re.rating +- 0.1
      }
    }
  }

  val expectedGrm = Seq(
    ("ingrid bernd", Seq(
      RE(0.29, List("in", "bred", "grind")),
      RE(0.29, List("in", "grind", "bred")))),
  )

  for ((srcText, expSeq) <- expectedGrm) {
    val cfg = Configurations.grammar

    val rdesc = cfg.cfgRaterAi.shortDescription
    val desc = expSeq.flatMap(re => re.words).mkString(" ")
    val rater = new RaterAi(cfg.cfgRaterAi.cfgRaterAi())
    val anagrams: Seq[Ana] = SolverRatedImpl(baseSolver, rater).solve(srcText).toSeq
    for (i <- 0 to 1) {
      ignore(s"$rdesc $srcText $i $desc") {
        val re = expSeq(i)
        val ana = anagrams(i)
        ana.sentence mustBe re.words
        ana.rate mustBe re.rating +- 0.1
      }
    }
  }

  val expectedGrmRed = Seq(
    ("ingrid bernd", Seq(
      RE(1.71, List("in", "bred", "grind")),
      RE(1.71, List("in", "grind", "bred")))),
  )

  for ((srcText, expSeq) <- expectedGrmRed) {
    val cfg = Configurations.grammarReduced

    val rdesc = cfg.cfgRaterAi.shortDescription
    val desc = expSeq.flatMap(re => re.words).mkString(" ")
    val rater = new RaterAi(cfg.cfgRaterAi.cfgRaterAi())
    val anagrams: Seq[Ana] = SolverRatedImpl(baseSolver, rater).solve(srcText).toSeq
    for (i <- 0 to 1) {
      ignore(s"$rdesc $srcText $i $desc") {
        val re = expSeq(i)
        val ana = anagrams(i)
        ana.sentence mustBe re.words
        ana.rate mustBe re.rating +- 0.1
      }
    }
  }

  val expectedPlainRated = Seq(
    ("ingrid bernd", Seq(
      RE(1.71, List("in", "bred", "grind")),
      RE(1.71, List("in", "grind", "bred")))),
  )

  for ((srcText, expSeq) <- expectedPlainRated) {
    val cfg = Configurations.plainRated

    val rdesc = cfg.cfgRaterAi.shortDescription
    val desc = expSeq.flatMap(re => re.words).mkString(" ")
    val rater = new RaterAi(cfg.cfgRaterAi.cfgRaterAi())
    val anagrams: Seq[Ana] = SolverRatedImpl(baseSolver, rater).solve(srcText).toSeq
    for (i <- 0 to 1) {
      ignore(s"$rdesc $srcText $i $desc") {
        val re = expSeq(i)
        val ana = anagrams(i)
        ana.sentence mustBe re.words
        ana.rate mustBe re.rating +- 0.1
      }
    }
  }

}
