package anagram.solve

import anagram.common.Cancelable

trait Solver extends Cancelable {

  def solve(srcText: String): Iterator[Seq[String]]

}
