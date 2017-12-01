package anagram.ml.data.analyze.training

import entelijan.viz.{Viz, VizCreator, VizCreatorGnuplot}

object DiaScorePlainB extends App {

  implicit val creator: VizCreator[Viz.XY] = VizCreatorGnuplot[Viz.XY]()


  def toXY(v: (Int, Double)): Viz.XY = Viz.XY(v._1, v._2)
  val data2 = Seq(
    (0, 1.2915371875),
    (30, 0.391580703125),
    (60, 0.06486818359375),
    (90, 0.015561688232421876),
    (120, 0.006503633422851563),
    (150, 0.005458704223632813),
    (180, 0.005146945190429688),
    (210, 0.004850748596191406),
    (240, 0.004440814208984375),
    (270, 0.004156759643554687),
    (300, 0.004053186131549607),
  ).map(toXY)



  val data3 = Seq(
    (0, 4.3832453125),
    (30, 1.41042515625),
    (60, 0.3967391796875),
    (90, 0.1499303515625),
    (120, 0.1217292578125),
    (150, 0.143856083984375),
    (180, 0.120588662109375),
    (210, 0.12500677971644839),
  ).map(toXY)



  val data4 = Seq(
    (0, 30.2894375),
    (30, 10.97708625),
    (60, 4.146116875),
    (90, 3.536006875),
    (120, 4.339704375),
    (150, 3.237600883123468),
  ).map(toXY)



  val data5 = Seq(
    (0, 403.18336),
    (30, 144.36252),
    (60, 48.429555),
    (90, 37.59277),
    (120, 30.2585075),
  ).map(toXY)


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
      id = "scorePlain03b",
      title = "ScorePlain bs: 50000 lr: 1E-5",
      legendPlacement = Viz.LegendPlacement_RIGHT,
      xRange = Some(Viz.Range(Some(0.0), Some(150))),
      yRange = Some(Viz.Range(Some(0.0), Some(800))),
      dataRows = dataRows
    )
    Viz.createDiagram(dia)
  }

  {
    val dia = Viz.Diagram[Viz.XY](
      id = "scorePlain03b_detail",
      title = "ScorePlain bs: 100000 lr: 1E-5 detail",
      legendPlacement = Viz.LegendPlacement_RIGHT,
      xRange = Some(Viz.Range(Some(0.0), Some(150))),
      yRange = Some(Viz.Range(Some(0.0), Some(6))),
      dataRows = dataRows
    )
    Viz.createDiagram(dia)
  }


}
