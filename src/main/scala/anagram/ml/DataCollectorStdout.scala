package anagram.ml

import anagram.model.SentenceLength

class DataCollectorStdout extends DataCollector {

  case class Score(sentenceLength: SentenceLength, iterations: Int, score: Double)

  var scores = List.empty[Score]

  override def collectScore(sentenceLength: SentenceLength, iterations: Int, score: Double): Unit = {
    scores = Score(sentenceLength, iterations, score) :: scores
  }

  def output(): Unit = {
    println("""// To be used in anagram.ml.data.analyze.DiaScore""")
    val datas = scores.reverse
      .groupBy(_.sentenceLength)
      .toSeq
      .sortBy(_._1.additionalId)
      .sortBy(_._1.length)
    val dataStr = for ((sl, scs) <- datas) yield {
      val id = sl.length + sl.additionalId.getOrElse("")
      val desc = sl.length + sl.additionalId.map(aid => s", $aid").getOrElse("")

      val values = scs
        .map(valuesLine)
        .mkString("\n")
      s"""
         |  val dataRow$id =    Viz.DataRow(
         |    name = Some("$desc"),
         |    style = Viz.Style_LINES,
         |    data = Seq(
         |      $values
         |    ).map(toXY)
         |  )
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
