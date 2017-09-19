package anagram.solve

import java.nio.file.Path
import java.util
import java.util.stream

import com.parthparekh.algorithms.AnagramSolver

import scala.collection.JavaConverters._


object Solver {

  def solve(src: String, dict: Path): Seq[Seq[String]] = {
    val solver = new AnagramSolver(3, dict.toFile)

    solver.findAllAnagrams(src.toLowerCase)
      .iterator()
      .asScala
      .toStream
      .map(_.asScala.toSeq)
  }

}
