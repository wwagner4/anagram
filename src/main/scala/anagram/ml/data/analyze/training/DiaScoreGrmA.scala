package anagram.ml.data.analyze.training

import entelijan.viz.{Viz, VizCreator, VizCreatorGnuplot}

object DiaScoreGrmA extends App {

  implicit val creator: VizCreator[Viz.XY] = VizCreatorGnuplot[Viz.XY]()

  val data2 = Seq(
    (0, 68.00586170848267),
    (100, 67.78298611111111),
    (200, 67.6343432646356),
    (300, 67.54558691756273),
    (400, 67.49243018219833),
    (500, 67.46047080346476),
    (600, 67.44100955794504),
    (700, 67.42900612305854),
    (800, 67.42146430704898),
    (900, 67.41650798984469),
    (1000, 67.41311977299881),
    (1100, 67.41065561529271),
    (1200, 67.4087328255675),
    (1300, 67.40709938769415),
    (1400, 67.40571796594982),
    (1500, 67.40446255227002),
    (1600, 67.40328181003584),
    (1700, 67.40211506869773),
    (1800, 67.40096232825567),
    (1900, 67.3998889262246),
  ).map(toXY)


  def toXY(v: (Int, Double)): Viz.XY = Viz.XY(v._1, v._2)


  val dataRows = Seq(
    Viz.DataRow(
      name = Some("2"),
      style = Viz.Style_LINES,
      data = data2,
    ),
  )

  {
    val dia = Viz.Diagram[Viz.XY](
      id = "scoreGrm12",
      title = "Score Grammar",
      legendPlacement = Viz.LegendPlacement_RIGHT,
      //yRange = Some(Viz.Range(Some(-100.0), Some(800))),
      dataRows = dataRows
    )
    Viz.createDiagram(dia)
  }


}
