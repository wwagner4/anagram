package anagram.ml.data.analyze.training

import entelijan.viz.{Viz, VizCreator, VizCreatorGnuplot}

object DiaScorePlainA extends App {

  implicit val creator: VizCreator[Viz.XY] = VizCreatorGnuplot[Viz.XY]()

  val data2 = Seq(
    (0, 1050.8068),
    (10, 828.474),
    (20, 555.3523),
    (30, 346.388325),
    (40, 210.0139),
    (50, 169.3292),
    (60, 103.1354625),
    (70, 56.1862375),
    (80, 71.9774875),
    (90, 66.2982875),
    (100, 38.1761125),
    (110, 79.798475),
    (120, 46.06543125),
    (130, 67.74466875),
    (140, 54.67523125),
    (150, 45.941525),
    (160, 54.24982118333715),
  ).map(toXY)


  val data3 = Seq(
    (0, 1118.0066),
    (10, 886.2097),
    (20, 553.0173),
    (30, 334.358575),
    (40, 250.7545),
    (50, 193.19065),
    (60, 83.0585625),
    (70, 86.2851),
    (80, 75.75184375),
    (90, 40.862375),
    (100, 97.56619375),
    (110, 46.261975),
    (120, 65.97014375),
    (130, 59.257375),
    (140, 64.0851125),
  ).map(toXY)


  val data4 = Seq(
    (0, 1101.4546),
    (10, 857.7099),
    (20, 533.89255),
    (30, 391.76885),
    (40, 326.11145),
    (50, 127.858275),
    (60, 80.0091375),
    (70, 89.07020625),
    (80, 50.936090625),
    (90, 114.3405),
    (100, 75.25205),
    (110, 60.7976875),
    (120, 54.9083125),
  ).map(toXY)


  val data5 = Seq(
    (0, 1214.134),
    (10, 912.9319),
    (20, 644.6156),
    (30, 444.1046),
    (40, 270.94215),
    (50, 184.031825),
    (60, 187.753375),
    (70, 65.56720625),
    (80, 65.64948125),
    (90, 87.36585),
    (100, 79.4928125),
    (110, 50.743984375),
    (120, 117.0802),
    (130, 58.6686375),
    (140, 100.31695),
    (150, 68.9522375),
    (160, 75.9439125),
    (170, 73.989075),
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
      id = "scorePlain04",
      title = "ScorePlain bs: 10000 lr: 1E-5",
      legendPlacement = Viz.LegendPlacement_RIGHT,
      //yRange = Some(Viz.Range(Some(-100.0), Some(800))),
      dataRows = dataRows
    )
    Viz.createDiagram(dia)
  }


}
