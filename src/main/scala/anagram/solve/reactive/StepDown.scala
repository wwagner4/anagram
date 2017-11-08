package anagram.solve.reactive

sealed trait StepDownRet

case class StepDownRet_CONTINUE(
                         first: String,
                         rest: String,
                       ) extends StepDownRet

case class StepDownRet_FINISHED(
                         rest: String,
                       ) extends StepDownRet

case class StepDownCfg (
                       maxDepth: Int,
                       )

trait StepDown {
  def step(txt: String, depth: Int)(implicit cfg: StepDownCfg): Iterable[StepDownRet]
}

object StepDownDumb extends StepDown {
  def step(txt: String, depth: Int)(implicit cfg: StepDownCfg): Iterable[StepDownRet] = {
    if (depth > cfg.maxDepth) Iterable.empty
    else
      txt.toList.map { c =>
        val first = c.toString
        if (first == "a") {
          StepDownRet_FINISHED(first)
        } else {
          val rest = s"${c.toString}${depth}a"
          StepDownRet_CONTINUE(first, rest)
        }
      }
  }
}
