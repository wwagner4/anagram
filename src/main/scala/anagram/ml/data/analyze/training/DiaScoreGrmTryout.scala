package anagram.ml.data.analyze.training

import entelijan.viz.{Viz, VizCreator, VizCreatorGnuplot}

object DiaScoreGrmTryout extends App {

  implicit val creator: VizCreator[Viz.XY] = VizCreatorGnuplot[Viz.XY]()

  val dataRowa =    Viz.DataRow(
    name = Some("10e-6"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 34.897010020559144),
      (10, 34.360072361711424),
      (20, 33.66200848267042),
      (30, 33.14745115179771),
      (40, 32.837398620756645),
      (50, 32.66914752399916),
      (60, 32.583470933903165),
      (70, 32.54159584284396),
      (80, 32.5216113836142),
      (90, 32.51225859194146),
    ).map(toXY)
  )

  val dataRowb =    Viz.DataRow(
    name = Some("50e-6"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 34.897010020559144),
      (10, 33.04788904537581),
      (20, 32.5068577695757),
      (30, 32.56724620789018),
      (40, 32.520939569067394),
      (50, 32.50270142295177),
      (60, 32.503630226798116),
      (70, 32.5019749728846),
      (80, 32.50068395577356),
      (90, 32.50009105920062),
    ).map(toXY)
  )

  val dataRowc =    Viz.DataRow(
    name = Some("100e-6"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 34.897010020559144),
      (10, 32.533983293671994),
      (20, 32.66999740987163),
      (30, 32.50620011979344),
      (40, 32.51269769964224),
      (50, 32.500451248927526),
      (60, 32.499585174752724),
      (70, 32.49777410842925),
      (80, 32.49666925679504),
      (90, 32.4956453466725),
    ).map(toXY)
  )


  val dataRows = Seq(
    dataRowa,
    dataRowb,
    dataRowc,
  )

  def toXY(v: (Int, Double)): Viz.XY = Viz.XY(v._1, v._2)

  {
    val dia = Viz.Diagram[Viz.XY](
      id = "scoreGrm4_04",
      title = "Score Grammar",
      legendPlacement = Viz.LegendPlacement_RIGHT,
      //yRange = Some(Viz.Range(Some(0.0), Some(42.0))),
      dataRows = dataRows
    )
    Viz.createDiagram(dia)
  }


}
