package anagram.ml

import anagram.model.SentenceLength
import entelijan.viz.Viz.Dia
import entelijan.viz.{DefaultDirectories, Viz, VizCreator, VizCreatorGnuplot}

case class DataCollectorViz(diaId: String, diaTitle: String) extends DataCollector {

  case class Model(name: String, scores: List[Score])

  case class Score(sentenceLength: SentenceLength, iterations: Int, score: Double)

  var models = List.empty[Model]

  override def collectScore(sentenceLength: SentenceLength, iterations: Int, score: Double): Unit = {
    models match {
      case Nil => throw new IllegalStateException("No model defined before collecting store :(")
      case m :: rest =>
        val scores_ = Score(sentenceLength, iterations, score) :: m.scores
        val m1 = m.copy(scores = scores_)
        models = m1 :: rest
    }
  }

  def nextModel(name: String): Unit = {
    val m = Model(name, List.empty[Score])
    models = m :: models
  }

  val dir = DefaultDirectories("ana")
  implicit val creator: VizCreator[Viz.XY] = VizCreatorGnuplot[Viz.XY](scriptDir = dir.scriptDir, imageDir = dir.imageDir)

  def output(): Unit = {

    def toDiagram(m: Model): Viz.Diagram[Viz.XY] =
      Viz.Diagram[Viz.XY](
        m.name,
        m.name,
        dataRows = toDataRows(m.scores)
      )

    def toDataRows(scores: List[Score]): Seq[Viz.DataRow[Viz.XY]] = {

      val datas: Seq[(SentenceLength, List[Score])] = scores
        .reverse
        .groupBy(_.sentenceLength)
        .toSeq
        .sortBy(_._1.id)
        .sortBy(_._1.length)

      for ((sl, scores_) <- datas) yield {
        Viz.DataRow[Viz.XY](
          name = Some(sl.desc),
          style = Viz.Style_LINES,
          data = toData(scores_)
        )
      }
    }

    def toData(scores: List[Score]): Seq[Viz.XY] =
      scores.map { score =>
        Viz.XY(score.iterations, score.score)
      }


    val toDia: Dia[Viz.XY] =
      if (models.size == 1) {
        val dia = toDiagram(models(0))
        dia.copy(id = diaId, title = diaTitle)
      } else if (models.size > 1) {
        Viz.MultiDiagram[Viz.XY](
          diaId,
          2,
          title = Some(diaTitle),
          diagrams = models.reverse.map(m => toDiagram(m))
        )
      } else {
        throw new IllegalStateException("At least one model expected")
      }

    Viz.createDiagram(toDia)

  }

}
