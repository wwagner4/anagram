package anagram.solve

import java.nio.file.Path

import com.parthparekh.algorithms.AnagramSolver

import scala.collection.JavaConverters._


object Solver {

  def solve(src: String, dict: Path): Stream[Iterable[String]] = {
    val solver = new AnagramSolver(2, dict.toFile)
    solver.findAllAnagrams(src.toLowerCase)
      .asScala.map(_.asScala.toSet)
      .iterator
      .toStream
  }

}
