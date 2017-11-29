package anagram.ml

class DataCollectorStdout extends DataCollector {

  case class Score(sentenceLength: Int, iterations: Int, score: Double)

  var scores = List.empty[Score]

  override def collectScore(sentenceLength: Int, iterations: Int, score: Double): Unit = {
    scores = Score(sentenceLength, iterations, score) :: scores
  }

  def output: Unit = {
    println("""// To bi used in anagram.ml.data.analyze.DiaScore""")
    for((l, scs) <- scores.reverse
      .groupBy(_.sentenceLength)
      .toSeq
      .sortBy(_._1)) {
      val values = scs
        .map(valuesLine)
        .mkString("\n")
      s"""
        |  val data$l. = Seq(
        |  $values
        |  ).map(toXY)
        |
      """.stripMargin
    }
  }

  def valuesLine(score: Score): String = {
    val a = score.iterations
    val b = score.score
    f"""    ($a, $b),"""
  }

}
