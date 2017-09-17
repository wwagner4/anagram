package anagram.solve

import java.nio.file.Path
import java.util
import java.util.stream

import com.parthparekh.algorithms.AnagramSolver

import scala.collection.JavaConverters._


object Solver {

  def solve(src: String, dict: Path): Stream[Iterable[String]] = {
    val solver = new AnagramSolver(3, dict.toFile)

    val x: stream.Stream[util.Set[String]] = solver.findAllAnagrams(src.toLowerCase)
    val sstr: Stream[util.Set[String]] = x.iterator().asScala.toStream
    sstr.map(d => d.asScala.toList).flatMap(_.permutations)
  }

}
