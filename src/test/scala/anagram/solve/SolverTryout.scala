package anagram.solve

object SolverTryout extends App {

  val re = Solver.solve("Ditschi ist fleissig")
  println(re.take(10).map(_.mkString(" ")).mkString("\n"))

}
