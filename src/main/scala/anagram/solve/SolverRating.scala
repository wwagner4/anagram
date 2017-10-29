package anagram.solve

import anagram.words.Word

case class SolverRating(parentSolver: Solver, rater: Rater) extends Solver {

  def solve(srcText: String, wordlist: Iterable[Word]): Stream[Ana] = {
    parentSolver.solve(srcText, wordlist)
      .map(parentAna => Ana(rater.rate(parentAna.sentence) * parentAna.rate, parentAna.sentence))
  }
}

