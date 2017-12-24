package anagram.ml.data.analyze.training

import entelijan.viz.{Viz, VizCreator, VizCreatorGnuplot}

object DiaScorePlainRatedA extends App {

  implicit val creator: VizCreator[Viz.XY] = VizCreatorGnuplot[Viz.XY]()

  def toXY(v: (Int, Double)): Viz.XY = Viz.XY(v._1, v._2)


  val dataRow2 = Viz.DataRow(
    name = Some("len:2"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 44.2859125),
      (10, 31.62196875),
      (20, 22.6223546875),
      (30, 15.795609375),
      (40, 9.2468078125),
      (50, 5.549308203125),
      (60, 6.6520859375),
      (70, 3.476616796875),
      (80, 3.045719140625),
      (90, 4.25645546875),
      (100, 4.333816015625),
      (110, 2.6972876953125),
      (120, 5.350323828125),
      (130, 3.777458984375),
      (140, 5.52101171875),
      (150, 3.68611015625),
      (160, 3.645326171875),
      (170, 3.1750919921875),
    ).map(toXY)
  )


  val dataRow3 = Viz.DataRow(
    name = Some("len:3"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 110.60395),
      (10, 83.02658125),
      (20, 56.64974375),
      (30, 36.5676125),
      (40, 27.7308375),
      (50, 26.523628125),
      (60, 18.4898234375),
      (70, 10.42570625),
      (80, 11.31215),
      (90, 9.14560078125),
      (100, 5.96365),
      (110, 15.25494375),
      (120, 10.11895625),
      (130, 12.84986328125),
      (140, 8.57196875),
      (150, 8.481171875),
      (160, 8.66270859375),
    ).map(toXY)
  )


  val dataRow4 = Viz.DataRow(
    name = Some("len:4"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 176.75085),
      (10, 135.7687),
      (20, 86.08715),
      (30, 51.84255),
      (40, 37.0270875),
      (50, 28.818946875),
      (60, 14.240159375),
      (70, 13.463340625),
      (80, 18.82826875),
      (90, 13.590975),
      (100, 26.29075),
      (110, 27.0454875),
      (120, 17.922790625),
      (130, 15.48526875),
      (140, 16.7345640625),
      (150, 15.970675),
    ).map(toXY)
  )


  val dataRow5 = Viz.DataRow(
    name = Some("len:5"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 253.75085),
      (10, 183.5782),
      (20, 129.53675),
      (30, 73.16426875),
      (40, 67.2843),
      (50, 33.16869375),
      (60, 36.21306875),
      (70, 52.0357375),
      (80, 53.6860375),
      (90, 26.50653125),
      (100, 20.578734375),
      (110, 34.1697625),
      (120, 28.741325),
      (130, 23.3694140625),
      (140, 25.700484375),
      (150, 45.33361875),
      (160, 27.875125),
      (170, 31.52198125),
      (180, 27.01335),
      (190, 26.294246875),
      (200, 29.351553125),
      (210, 27.7664375),
    ).map(toXY)
  )


  val dataRows = Seq(
    dataRow2,
    dataRow3,
    dataRow4,
    dataRow5,
  )

  {
    val dia = Viz.Diagram[Viz.XY](
      id = "scorePlainRated01",
      title = "Score Plain Rated",
      legendPlacement = Viz.LegendPlacement_RIGHT,
      //yRange = Some(Viz.Range(Some(14.0), Some(16.0))),
      dataRows = dataRows
    )
    Viz.createDiagram(dia)
  }


}
