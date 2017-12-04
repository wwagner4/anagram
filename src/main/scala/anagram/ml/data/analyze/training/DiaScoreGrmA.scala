package anagram.ml.data.analyze.training

import entelijan.viz.{Viz, VizCreator, VizCreatorGnuplot}

object DiaScoreGrmA extends App {

  implicit val creator: VizCreator[Viz.XY] = VizCreatorGnuplot[Viz.XY]()


  val data2 = Seq(
    (0, 69.80222334229391),
    (100, 68.83636686081242),
    (200, 68.21341099163679),
    (300, 67.85443735065711),
    (400, 67.64733142921148),
    (500, 67.52766577060932),
    (600, 67.4583660020908),
    (700, 67.41809475806451),
    (800, 67.39456391875747),
    (900, 67.38068436379929),
    (1000, 67.37237716547192),
    (1100, 67.36726217144565),
    (1200, 67.36400462962963),
    (1300, 67.36181115591398),
    (1400, 67.36022438769415),
    (1500, 67.35902964456392),
    (1600, 67.3580309139785),
    (1700, 67.35715352449223),
    (1800, 67.35637880824373),
    (1900, 67.35565076164875),
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
      id = "scoreGrm08",
      title = "Score Grammar",
      legendPlacement = Viz.LegendPlacement_RIGHT,
      //yRange = Some(Viz.Range(Some(-100.0), Some(800))),
      dataRows = dataRows
    )
    Viz.createDiagram(dia)
  }


}
