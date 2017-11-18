package anagram.solve

import anagram.common.Cancelable
import anagram.words.Word

case class Ana(rate: Double, sentence: Iterable[String])

trait SolverRated extends Cancelable {

  def solve(srcText: String, wordlist: Iterable[Word]): Iterator[Ana]

}
