package anagram.ml.data.analyze.training

import entelijan.viz.{Viz, VizCreator, VizCreatorGnuplot}

object DiaScorePlainA extends App {

  implicit val creator: VizCreator[Viz.XY] = VizCreatorGnuplot[Viz.XY]()


  val dataRow2 = Viz.DataRow(
    name = Some("len:2"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 1073.1328),
      (10, 847.2732),
      (20, 568.85945),
      (30, 355.4226),
      (40, 216.3843),
      (50, 173.900175),
      (60, 105.5512125),
      (70, 57.029725),
      (80, 72.430375),
      (90, 66.4271625),
      (100, 38.0118375),
      (110, 79.725225),
      (120, 45.98476875),
      (130, 67.650925),
      (140, 54.555625),
      (150, 45.881575),
      (160, 54.14068579766537),
    ).map(toXY)
  )


  val dataRow3 = Viz.DataRow(
    name = Some("len:3"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 1156.5486),
      (10, 919.527),
      (20, 577.64055),
      (30, 351.91895),
      (40, 262.4889),
      (50, 200.348425),
      (60, 85.1637875),
      (70, 87.0986625),
      (80, 75.7908125),
      (90, 40.2099875),
      (100, 97.18171875),
      (110, 45.7625),
      (120, 65.373075),
      (130, 58.752925),
      (140, 63.86950625),
    ).map(toXY)
  )


  val dataRow4 = Viz.DataRow(
    name = Some("len:4"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 1112.4067),
      (10, 863.6966),
      (20, 534.7122),
      (30, 388.86165),
      (40, 321.58845),
      (50, 125.213825),
      (60, 78.15225),
      (70, 87.6657625),
      (80, 50.65948125),
      (90, 113.777625),
      (100, 75.28105),
      (110, 61.0204875),
      (120, 55.004725),
    ).map(toXY)
  )


  val dataRow5 = Viz.DataRow(
    name = Some("len:5"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 1126.425),
      (10, 831.1353),
      (20, 570.2791),
      (30, 381.26645),
      (40, 225.4701),
      (50, 153.1254),
      (60, 166.88965),
      (70, 57.690925),
      (80, 62.08),
      (90, 85.10335625),
      (100, 78.7118125),
      (110, 50.757775),
      (120, 116.687775),
      (130, 58.5865),
      (140, 100.1393125),
      (150, 68.8064375),
      (160, 75.7784375),
      (170, 73.907925),
    ).map(toXY)
  )

  def toXY(v: (Int, Double)): Viz.XY = Viz.XY(v._1, v._2)

  val dia = Viz.Diagram[Viz.XY](
    id = "scorePlain06",
    title = "Score Plain",
    legendPlacement = Viz.LegendPlacement_RIGHT,
    //yRange = Some(Viz.Range(Some(-100.0), Some(800))),
    dataRows = Seq(
      dataRow2,
      dataRow3,
      dataRow4,
      dataRow5,
    )
  )
  Viz.createDiagram(dia)


}
