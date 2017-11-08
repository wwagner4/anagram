package anagram.solve.reactive

object StepDownTryout extends App {

  val start = "anna"
  val rd: StepDown = StepDownDumb

  implicit val cfg = StepDownCfg(maxDepth = 2)

  val x = recDes(rd, "", start, 0).mkString("\n")
  println(x)

  def recDes(rd: StepDown, headText: String, txt: String, depth: Int): Iterable[String] = {
    val x = rd.step(txt, depth)
    x.flatMap {
      case StepDownRet_FINISHED(a) => Seq(headText + a)
      case StepDownRet_CONTINUE(a, b) => recDes(rd, headText + a, b, depth + 1)
    }
  }

}
