package anagram.ml.data.analyze.training

import entelijan.viz.{Viz, VizCreator, VizCreatorGnuplot}

object DiaScoreGrmTryout extends App {



  implicit val creator: VizCreator[Viz.XY] = VizCreatorGnuplot[Viz.XY]()


  val dataRowa =    Viz.DataRow(
    name = Some("bs:10k"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 39.71793125),
      (1, 39.4120625),
      (2, 38.9969875),
      (3, 38.5037625),
      (4, 37.96218125),
      (5, 37.399728125),
      (6, 36.839921875),
      (7, 36.302521875),
      (8, 35.80243125),
      (9, 35.35055625),
      (10, 34.95365),
      (11, 34.6156),
      (12, 34.335740625),
      (13, 34.111996875),
      (14, 33.940209375),
      (15, 33.814821875),
      (16, 33.729534375),
      (17, 33.67761875),
      (18, 33.6524),
      (19, 33.647675),
      (20, 33.657490625),
      (21, 33.676834375),
      (22, 33.701165625),
      (23, 33.7271625),
      (24, 33.7522125),
      (25, 33.773975),
      (26, 33.791584375),
      (27, 33.804065625),
      (28, 33.8112625),
      (29, 33.813375),
      (30, 33.8107875),
      (31, 33.80419375),
      (32, 33.794384375),
      (33, 33.7821375),
      (34, 33.768271875),
      (35, 33.753446875),
      (36, 33.73845),
      (37, 33.72379375),
      (38, 33.7099),
      (39, 33.69721875),
      (40, 33.685890625),
      (41, 33.676084375),
      (42, 33.667784375),
      (43, 33.661021875),
      (44, 33.65570625),
      (45, 33.651640625),
      (46, 33.648796875),
      (47, 33.646925),
      (48, 33.64584375),
      (49, 33.64538125),
      (50, 43.390402397260274),
      (51, 43.396656678082195),
      (52, 43.40352739726028),
      (53, 43.410299657534246),
      (54, 43.41634417808219),
      (55, 43.421339897260275),
      (56, 43.425021404109586),
      (57, 43.42720890410959),
      (58, 43.42806934931507),
      (59, 43.42760273972603),
      (60, 43.4260102739726),
      (61, 43.42347602739726),
      (62, 43.4202397260274),
      (63, 43.41648116438356),
      (64, 43.412431506849316),
      (65, 43.408343321917805),
      (66, 43.40414811643836),
      (67, 43.40023116438356),
      (68, 43.39656678082192),
      (69, 43.39325342465754),
      (70, 43.39037671232877),
      (71, 43.38782534246575),
      (72, 43.38577054794521),
      (73, 43.38404109589041),
      (74, 43.38271404109589),
      (75, 43.38167808219178),
      (76, 43.380907534246575),
      (77, 43.3803852739726),
      (78, 43.38004280821918),
      (79, 43.37985445205479),
      (80, 43.37970890410959),
      (81, 43.3796875),
      (82, 43.379683219178084),
      (83, 43.37970890410959),
      (84, 43.379640410958906),
      (85, 43.3796147260274),
      (86, 43.379537671232875),
      (87, 43.37943921232877),
      (88, 43.37928938356164),
      (89, 43.37912671232877),
      (90, 43.37893835616438),
      (91, 43.3786301369863),
      (92, 43.37840753424658),
      (93, 43.378120719178085),
      (94, 43.377876712328764),
      (95, 43.377564212328764),
      (96, 43.377251712328764),
      (97, 43.37697345890411),
      (98, 43.37671232876713),
      (99, 43.37640410958904),
    ).map(toXY)
  )



  val dataRowb =    Viz.DataRow(
    name = Some("bs:15k"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 42.41443223443223),
      (1, 42.10391025641026),
      (2, 41.68254120879121),
      (3, 41.18179945054945),
      (4, 40.632069597069595),
      (5, 40.061025641025644),
      (6, 39.492770146520144),
      (7, 38.947147435897435),
      (8, 38.43954212454212),
      (9, 37.980714285714285),
      (10, 37.5779097985348),
      (11, 37.23474358974359),
      (12, 36.95062728937729),
      (13, 36.72358974358974),
      (14, 36.549166666666665),
      (15, 36.42187042124542),
      (16, 36.33528846153846),
      (17, 36.28254120879121),
      (18, 36.257076465201465),
      (19, 36.25215201465201),
      (20, 36.26207875457875),
      (21, 36.281680402930405),
      (22, 36.306419413919414),
      (23, 36.33289835164835),
      (24, 36.35824862637362),
      (25, 36.380489926739926),
      (26, 36.39826465201465),
      (27, 36.41096611721612),
      (28, 36.418310439560436),
      (29, 36.42046016483516),
      (30, 36.417870879120876),
      (31, 36.41124771062271),
      (32, 36.40127976190476),
      (33, 36.388910256410256),
      (34, 36.374803113553114),
      (35, 36.35981227106227),
      (36, 36.344578754578755),
      (37, 36.329750457875456),
      (38, 36.31574404761905),
      (39, 36.30289606227106),
      (40, 36.291378205128204),
      (41, 36.2813576007326),
      (42, 36.272999084249086),
      (43, 36.26614697802198),
      (44, 36.26072573260073),
      (45, 36.256712454212455),
      (46, 36.253777472527474),
      (47, 36.25186355311355),
      (48, 36.2507760989011),
      (49, 36.250320512820515),
    ).map(toXY)
  )



  val dataRowc =    Viz.DataRow(
    name = Some("bs:20k"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 42.41443223443223),
      (1, 42.10391025641026),
      (2, 41.68254120879121),
      (3, 41.18179945054945),
      (4, 40.632069597069595),
      (5, 40.061025641025644),
      (6, 39.492770146520144),
      (7, 38.947147435897435),
      (8, 38.43954212454212),
      (9, 37.980714285714285),
      (10, 37.5779097985348),
      (11, 37.23474358974359),
      (12, 36.95062728937729),
      (13, 36.72358974358974),
      (14, 36.549166666666665),
      (15, 36.42187042124542),
      (16, 36.33528846153846),
      (17, 36.28254120879121),
      (18, 36.257076465201465),
      (19, 36.25215201465201),
      (20, 36.26207875457875),
      (21, 36.281680402930405),
      (22, 36.306419413919414),
      (23, 36.33289835164835),
      (24, 36.35824862637362),
      (25, 36.380489926739926),
      (26, 36.39826465201465),
      (27, 36.41096611721612),
      (28, 36.418310439560436),
      (29, 36.42046016483516),
      (30, 36.417870879120876),
      (31, 36.41124771062271),
      (32, 36.40127976190476),
      (33, 36.388910256410256),
      (34, 36.374803113553114),
      (35, 36.35981227106227),
      (36, 36.344578754578755),
      (37, 36.329750457875456),
      (38, 36.31574404761905),
      (39, 36.30289606227106),
      (40, 36.291378205128204),
      (41, 36.2813576007326),
      (42, 36.272999084249086),
      (43, 36.26614697802198),
      (44, 36.26072573260073),
      (45, 36.256712454212455),
      (46, 36.253777472527474),
      (47, 36.25186355311355),
      (48, 36.2507760989011),
      (49, 36.250320512820515),
    ).map(toXY)
  )



  val dataRows = Seq(
    dataRowa,
    dataRowb,
    dataRowc,
//    dataRowd,
//    dataRowe,
  )

  def toXY(v: (Int, Double)): Viz.XY = Viz.XY(v._1, v._2)

  {
    val dia = Viz.Diagram[Viz.XY](
      id = "scoreGrm3_10",
      title = "Score Grammar",
      legendPlacement = Viz.LegendPlacement_RIGHT,
      //yRange = Some(Viz.Range(Some(0.0), Some(42.0))),
      dataRows = dataRows
    )
    Viz.createDiagram(dia)
  }


}
