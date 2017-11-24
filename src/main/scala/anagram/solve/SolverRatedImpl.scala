package anagram.solve

import anagram.ml.rate.Rater

case class SolverRatedImpl(parentSolver: Solver, rater: Rater) extends SolverRated {

  override def solve(srcText: String): Iterator[Ana] = {
    parentSolver.solve(srcText)
      .map(parentAna => Ana(rater.rate(parentAna), parentAna))
  }

  override def cancel(): Unit = parentSolver.cancel()
}

