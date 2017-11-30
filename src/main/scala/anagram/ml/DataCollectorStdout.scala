package anagram.ml

class DataCollectorStdout extends DataCollector {

  case class Score(sentenceLength: Int, iterations: Int, score: Double)

  var scores = List.empty[Score]

  override def collectScore(sentenceLength: Int, iterations: Int, score: Double): Unit = {
    scores = Score(sentenceLength, iterations, score) :: scores
  }

  def output(): Unit = {
    println("""// To be used in anagram.ml.data.analyze.DiaScore""")
    val datas = scores.reverse
      .groupBy(_.sentenceLength)
      .toSeq.sortBy(_._1)
    val dataStr = for ((l, scs) <- datas) yield {
      val values = scs
        .map(valuesLine)
        .mkString("\n")
      s"""
         |  val data$l = Seq(
         |  $values
         |  ).map(toXY)
         |
      """.stripMargin
    }
    println(dataStr.mkString("\n"))
  }

  def valuesLine(score: Score): String = {
    val a = score.iterations
    val b = score.score
    f"""       ($a, $b),"""
  }

}
