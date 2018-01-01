package anagram.ml

import anagram.model.SentenceLength
import entelijan.viz.Viz.Dia
import entelijan.viz.{DefaultDirectories, Viz, VizCreator, VizCreatorGnuplot}

case class DataCollectorViz(diaId: String, diaTitle: String) extends DataCollector {

  case class Score(modelId: String, sentenceLength: SentenceLength, iterations: Int, score: Double)

  case class Model(name: String, scores: List[Score])

  var _scores = List.empty[Score]

  override def collectScore(modelId: String, sentenceLength: SentenceLength, iterations: Int, score: Double): Unit = {
    _scores = Score(modelId, sentenceLength, iterations, score) :: _scores

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


    def toDia(models: List[Model]): Dia[Viz.XY] =
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

    def models(scores: List[Score]): List[Model] = {
      val mg: Map[String, List[Score]] = scores.groupBy(s => s.modelId)
      for ((mid, scores) <- mg.toList) yield {
        Model(mid, scores)
      }
    }

    Viz.createDiagram(toDia(models(_scores)))

  }

}
