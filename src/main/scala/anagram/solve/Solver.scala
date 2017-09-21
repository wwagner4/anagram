package anagram.solve

import java.nio.file.Path

import com.parthparekh.algorithms.AnagramSolver

import scala.collection.JavaConverters._


object Solver {

  def solve(src: String, dict: Path): Seq[Seq[String]] = {
    val solver = new AnagramSolver(2, dict.toFile)

    solver.findAllAnagrams(src.toLowerCase)
      .iterator()
      .asScala
      .toStream
      .map(_.asScala.toSeq)
  }

}
