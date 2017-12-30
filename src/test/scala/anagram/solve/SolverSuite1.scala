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
    ("wolfgang wagner", Seq(
      RE(31.770, List("of", "grew", "gang", "lawn")),
      RE(31.817, List("of", "grew", "lawn", "gang")))),
    ("brigitte freimueller", Seq(
      RE(31.359, List("little", "i", "free", "urge", "brim")),
      RE(31.456, List("little", "i", "refuge", "re", "brim")))),
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
    ("wolfgang wagner", Seq(
      RE(22.10758, List("of", "grew", "gang", "lawn")),
      RE(22.50175, List("of", "grew", "lawn", "gang")))),
    ("clint eastwood", Seq(
      RE(22.657, List("and", "to", "is", "let", "cow")),
      RE(22.65767, List("and", "to", "is", "cow", "let")))),
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
    ("wolfgang wagner", Seq(
      RE(0.56, List("of", "grew", "gang", "lawn")),
      RE(0.52, List("of", "grew", "lawn", "gang")))),
    ("wolfgang brigitte", Seq(
      RE(0.50, List("of", "it", "grew", "bag", "glint")),
      RE(0.49, List("of", "it", "grew", "bang", "gilt")))),
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
    ("wolfgang wagner", Seq(
      RE(1.82, List("of", "grew", "gang", "lawn")),
      RE(1.80, List("of", "grew", "lawn", "gang")))),
    ("noah wagner", Seq(
      RE(1.82, List("her", "a", "an", "gown")),
      RE(1.80, List("her", "a", "gown", "an")))),
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
    ("wolfgang wagner", Seq(
      RE(1.82, List("of", "grew", "gang", "lawn")),
      RE(1.80, List("of", "grew", "lawn", "gang")))),
    ("noah wagner", Seq(
      RE(1.82, List("her", "a", "an", "gown")),
      RE(1.80, List("her", "a", "gown", "an")))),
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
