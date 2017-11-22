package anagram.solve

import anagram.ml.rate.Rater
import anagram.words.Word

case class SolverRatedImpl(parentSolver: Solver, rater: Rater) extends SolverRated {

  override def solve(srcText: String, wordlist: Iterable[Word]): Iterator[Ana] = {
    parentSolver.solve(srcText, wordlist)
      .map(parentAna => Ana(rater.rate(parentAna), parentAna))
  }

  override def cancel(): Unit = parentSolver.cancel()
}

