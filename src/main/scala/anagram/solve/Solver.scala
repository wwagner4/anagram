package anagram.solve

import anagram.common.Cancelable
import anagram.words.Word

trait Solver extends Cancelable {

  def solve(srcText: String, wordlist: Iterable[Word]): Iterator[Iterable[String]]

}
