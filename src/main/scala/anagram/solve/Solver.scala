package anagram.solve

import anagram.words.Word

case class Ana(rate: Double, sentence: Iterable[String])

trait Solver {

  def solve(srcText: String, wordlist: Iterable[Word]): Stream[Ana]

}
