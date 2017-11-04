package anagram.solve

import anagram.words.Word

case class SolverRated(parentSolver: Solver, rater: Rater) extends Solver {

  override def solve(srcText: String, wordlist: Iterable[Word]): Iterator[Ana] = {
    parentSolver.solve(srcText, wordlist)
      .map(parentAna => Ana(rater.rate(parentAna.sentence) * parentAna.rate, parentAna.sentence))
  }

  override def cancel(): Unit = parentSolver.cancel()
}

