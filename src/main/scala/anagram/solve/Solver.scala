package anagram.solve

case class Ana(rate: Double, sentence: Iterable[String])

trait Solver {

  def solve(srcText: String, wordlist: Iterable[String]): Stream[Ana]

}
