package anagram.solve

import java.io.File

import com.parthparekh.algorithms.AnagramSolver

import scala.collection.JavaConverters._


object Solver {

  def solve(src: String): Stream[Iterable[String]] = {
    val solver = new AnagramSolver(3, new File("dummy"))
    solver.findAllAnagrams(src)
      .asScala.map(_.asScala.toSet)
      .iterator
      .toStream
  }

}
