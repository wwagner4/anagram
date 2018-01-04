package anagram.ml

import anagram.common.IoUtil
import anagram.model.SentenceLength
import entelijan.viz.Viz.Dia
import entelijan.viz.{Viz, VizCreator, VizCreatorGnuplot}

case class DataCollectorScore(modelId: String, sentenceLength: SentenceLength, iterations: Int, score: Double)

case class DataCollectorScoreSentenceLength(sentenceLength: SentenceLength, scores: List[DataCollectorScore])

case class DataCollectorScoreModel(name: String, scores: List[DataCollectorScoreSentenceLength])

case class DataCollectorViz(diaId: String, diaTitle: String) extends DataCollector {

  implicit val creator: VizCreator[Viz.XY] = VizCreatorGnuplot[Viz.XY](
    scriptDir = IoUtil.dirVizScripts.toFile,
    imageDir = IoUtil.dirVizImages.toFile
  )

  var _scores = List.empty[DataCollectorScore]

  override def collectScore(modelId: String, sentenceLength: SentenceLength, iterations: Int, score: Double): Unit = {
    _scores = DataCollectorScore(modelId, sentenceLength, iterations, score) :: _scores
  }

  def output(): Unit = {

    def models(scores: List[DataCollectorScore]): List[DataCollectorScoreModel] = {

      def sentenceLengths(scores: List[DataCollectorScore]): List[DataCollectorScoreSentenceLength] = {
        scores
          .reverse
          .groupBy(_.sentenceLength)
          .toList
          .sortBy(_._1.id)
          .sortBy(_._1.length)
          .map { case (sl, scs) => DataCollectorScoreSentenceLength(sl, scs) }
      }

      val mg: Map[String, List[DataCollectorScore]] = scores.groupBy(s => s.modelId)
      for ((mid, scores) <- mg.toList) yield {
        DataCollectorScoreModel(mid, sentenceLengths(scores))
      }
    }

    Viz.createDiagram(toDia(models(_scores)))

  }

  protected def toDiagram(scoreModel: DataCollectorScoreModel): Viz.Diagram[Viz.XY] =
    Viz.Diagram[Viz.XY](
      scoreModel.name,
      scoreModel.name,
      legendPlacement = Viz.LegendPlacement_RIGHT,
      dataRows = toDataRows(scoreModel.scores)
    )

  protected def toDataRows(sentenceLengthList: List[DataCollectorScoreSentenceLength]): Seq[Viz.DataRow[Viz.XY]] =
    for (sl <- sentenceLengthList) yield {
      Viz.DataRow[Viz.XY](
        name = Some(sl.sentenceLength.desc),
        style = Viz.Style_LINES,
        data = toData(sl.scores)
      )
    }

  protected def toData(scores: List[DataCollectorScore]): Seq[Viz.XY] =
    scores.map(score => Viz.XY(score.iterations, score.score))

  protected def toDia(models: List[DataCollectorScoreModel]): Dia[Viz.XY] =
    if (models.lengthCompare(1) == 0) {
      val dia = toDiagram(models(0))
      dia.copy(id = diaId, title = diaTitle)
    } else if (models.lengthCompare(1) > 0) {
      Viz.MultiDiagram[Viz.XY](
        diaId,
        2,
        imgWidth = 900,
        imgHeight = 1500,
        title = Some(diaTitle),
        diagrams = models.reverse.map(m => toDiagram(m))
      )
    } else {
      throw new IllegalStateException("At least one model expected")
    }

}
