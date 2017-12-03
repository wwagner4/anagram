package anagram.ml.data.analyze.training

import entelijan.viz.{Viz, VizCreator, VizCreatorGnuplot}

object DiaScorePlainA extends App {

  implicit val creator: VizCreator[Viz.XY] = VizCreatorGnuplot[Viz.XY]()

  val data2 = Seq(
    (0, 1041.375),
    (10, 819.6082),
    (20, 547.41015),
    (30, 339.5124),
    (40, 206.2793),
    (50, 167.4689625),
    (60, 102.2703625),
    (70, 55.46020625),
    (80, 71.6490875),
    (90, 66.05360625),
    (100, 38.01620625),
    (110, 79.77011875),
    (120, 45.991940625),
    (130, 67.6596625),
    (140, 54.499075),
    (150, 45.879490625),
    (160, 54.18990329594873),
  ).map(toXY)



  val data3 = Seq(
    (0, 1084.3884),
    (10, 858.772),
    (20, 532.5291),
    (30, 320.12985),
    (40, 240.283425),
    (50, 185.8331),
    (60, 78.8081),
    (70, 83.5366),
    (80, 74.29110625),
    (90, 40.13099375),
    (100, 96.86816875),
    (110, 45.731790625),
    (120, 65.48969375),
    (130, 58.8011125),
    (140, 63.8282375),
  ).map(toXY)



  val data4 = Seq(
    (0, 1026.2224),
    (10, 797.11765),
    (20, 492.3261),
    (30, 362.98065),
    (40, 305.53265),
    (50, 117.9074375),
    (60, 75.1547375),
    (70, 86.2856375),
    (80, 50.351475),
    (90, 113.6157375),
    (100, 75.338925),
    (110, 61.133375),
    (120, 55.093775),
  ).map(toXY)


  val data5 = Seq(
    (0, 1229.6378),
    (10, 910.6032),
    (20, 623.7719),
    (30, 415.3841),
    (40, 245.4444),
    (50, 164.34435),
    (60, 173.7738875),
    (70, 59.6237375),
    (80, 62.69178125),
    (90, 85.35250625),
    (100, 78.76945),
    (110, 50.529196875),
    (120, 116.625625),
    (130, 58.3428875),
    (140, 99.988825),
    (150, 68.5521125),
    (160, 75.72424375),
    (170, 73.8319875),
  ).map(toXY)



  def toXY(v: (Int, Double)): Viz.XY = Viz.XY(v._1, v._2)


  val dataRows = Seq(
    Viz.DataRow(
      name = Some("2"),
      style = Viz.Style_LINES,
      data = data2,
    ),
    Viz.DataRow(
      name = Some("3"),
      style = Viz.Style_LINES,
      data = data3,
    ),
    Viz.DataRow(
      name = Some("4"),
      style = Viz.Style_LINES,
      data = data4,
    ),
    Viz.DataRow(
      name = Some("5"),
      style = Viz.Style_LINES,
      data = data5,
    ),
  )

  {
    val dia = Viz.Diagram[Viz.XY](
      id = "scorePlain05",
      title = "ScorePlain bs: 10000 lr: 1E-5",
      legendPlacement = Viz.LegendPlacement_RIGHT,
      //yRange = Some(Viz.Range(Some(-100.0), Some(800))),
      dataRows = dataRows
    )
    Viz.createDiagram(dia)
  }


}
