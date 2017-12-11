package anagram.ml.data.analyze.training

import entelijan.viz.{Viz, VizCreator, VizCreatorGnuplot}

object DiaScoreGrmTryout extends App {

  implicit val creator: VizCreator[Viz.XY] = VizCreatorGnuplot[Viz.XY]()


  val dataRowa =    Viz.DataRow(
    name = Some("iter:100 lr50"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 34.744668997697694),
      (5, 32.34825726703028),
      (10, 29.766273099983994),
      (15, 28.51758738288416),
      (20, 28.311758984524086),
      (25, 28.42629550743016),
      (30, 28.474868571709983),
      (35, 28.42259581645593),
      (40, 28.35249867648326),
      (45, 28.314116691085037),
      (50, 28.305239895103604),
      (55, 28.30764069290718),
      (60, 28.309111951048347),
      (65, 28.307877694741638),
      (70, 28.306123265577483),
      (75, 28.304538123437943),
      (80, 28.303959469608362),
      (85, 28.303873287123107),
      (90, 28.30372862366571),
      (95, 28.303491621831256),
    ).map(toXY)
  )



  val dataRowb =    Viz.DataRow(
    name = Some("iter:100 lr:10"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 34.744668997697694),
      (5, 34.19883530527068),
      (10, 33.302863720867244),
      (15, 32.32766580894574),
      (20, 31.41766494712089),
      (25, 30.64991135515802),
      (30, 30.032515420508968),
      (35, 29.56120187631582),
      (40, 29.203569186067984),
      (45, 28.93907513881536),
      (50, 28.747940854191548),
      (55, 28.613323812220678),
      (60, 28.51788286569075),
      (65, 28.450842125998793),
      (70, 28.40483606860126),
      (75, 28.373105524297305),
      (80, 28.351652241360206),
      (85, 28.336819620058357),
      (90, 28.327111778683378),
      (95, 28.320592689262895),
    ).map(toXY)
  )



  val dataRowc =    Viz.DataRow(
    name = Some("iter:100 lr:5"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 34.744668997697694),
      (5, 34.467721581325485),
      (10, 33.989141006857665),
      (15, 33.42743434741391),
      (20, 32.85114438028637),
      (25, 32.30153404823757),
      (30, 31.792811149551234),
      (35, 31.331593267916723),
      (40, 30.926517119535106),
      (45, 30.573015032687785),
      (50, 30.262044002314614),
      (55, 29.99309616734176),
      (60, 29.76144380286372),
      (65, 29.563467244499712),
      (70, 29.38916316806816),
      (75, 29.239405710205236),
      (80, 29.109045467416866),
      (85, 28.996583480048756),
      (90, 28.900982480331926),
      (95, 28.817844083572385),
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
      id = "scoreGrm5_04",
      title = "Score Grammar length:5",
      legendPlacement = Viz.LegendPlacement_RIGHT,
      //yRange = Some(Viz.Range(Some(0.0), Some(42.0))),
      dataRows = dataRows
    )
    Viz.createDiagram(dia)
  }


}
