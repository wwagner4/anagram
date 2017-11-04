package anagram.solve
import anagram.words.{Word, WordMapper, WordMappers}

import scala.concurrent.ExecutionContext

case class CfgSolverAi(
                        id: String,
                        mapper: WordMapper,
                        adjustOutput: (Int, Double) => Double,
                      )

object CfgSolverAis {

  private def adjustOutputPlain(len: Int, rating: Double): Double = {
    if (len == 1) rating + 5 // Anagram existing of one word must always be top
    else if (len == 2) rating + 3.9
    else if (len == 3) rating + 1.5
    else if (len == 4) rating + 1.2
    else rating
  }

  private def adjustOutputGrammar(len: Int, rating: Double): Double = {
    if (len == 1) rating + 5 // Anagram existing of one word must always be top
    else if (len == 2) rating + 0.2
    else rating
  }

  val cfgPlain = CfgSolverAi("enPlain11", WordMappers.createWordMapperPlain, adjustOutputPlain)
  val cfgGrm = CfgSolverAi("enGrm11", WordMappers.createWordMapperGrammer, adjustOutputGrammar)


}

object WordLists {

  lazy val wordListIgnoring: Iterable[Word] = {
    val ignoreWords = Seq(
      "ere",
      "nth",
      "id",
      "dreg",
      "cal",
      "inc",
      "nevi",
      "von",
      "cit",
      "esc",
      "alt",
      "brin",
      "veer",
      "brin",
      "bin",
      "nil",
      "chi",
      "cd",
      "ohs",
      "lith",
      "noir",
      "veda",
      "vade",
      "vinal",
      "dict",
      "wonts",
      "wots",
      "odic",
      "orth",
      "dows",
      "thor",
      "ghee",
      "attn",
      "din",
      "led",
      "etc",
    ).toSet

    WordMappers.createWordMapperPlain
      .wordList
      .filter(w => !ignoreWords.contains(w.word))
  }

}


class SolverAi(cfg: CfgSolverAi)(implicit ec: ExecutionContext) extends Solver {

  val rater: Rater = new RaterAi(cfg.id, cfg.mapper, cfg.adjustOutput, None)
  //val rater: Rater = new RaterRandom
  val baseSolver = SolverImpl(maxDepth = 4, parallel = 5)
  val aiSolver = SolverRating(baseSolver, rater)

  override def solve(srcText: String, wordlist: Iterable[Word]): Iterator[Ana] = {
    aiSolver.solve(srcText, wordlist)
  }

  override def cancel(): Unit = aiSolver.cancel()
}
