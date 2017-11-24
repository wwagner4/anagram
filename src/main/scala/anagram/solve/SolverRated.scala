package anagram.solve

import anagram.common.Cancelable

case class Ana(rate: Double, sentence: Iterable[String])

trait SolverRated extends Cancelable {

  def solve(srcText: String): Iterator[Ana]

}
