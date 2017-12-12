package anagram.ml.data.analyze.training

import entelijan.viz.{Viz, VizCreator, VizCreatorGnuplot}

object DiaScoreGrmRedA extends App {

  implicit val creator: VizCreator[Viz.XY] = VizCreatorGnuplot[Viz.XY]()

  val dataRow2 =    Viz.DataRow(
    name = Some("len:2"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 657.2818080357143),
      (10, 651.4478635204082),
      (20, 641.6170280612245),
      (30, 630.7461336096939),
      (40, 619.8615274234694),
      (50, 609.3042889030612),
      (60, 599.1766980229592),
      (70, 589.498684630102),
      (80, 580.2626355229592),
      (90, 571.452128507653),
      (100, 563.0483896683673),
      (110, 555.0325255102041),
      (120, 547.3867984693877),
      (130, 540.0935905612245),
      (140, 533.13671875),
      (150, 526.500637755102),
      (160, 520.1708386479592),
      (170, 514.1330516581633),
      (180, 508.3742426658163),
      (190, 502.88177614795916),
      (200, 497.6436543367347),
      (210, 492.6484375),
      (220, 487.8852838010204),
      (230, 483.34378985969386),
      (240, 479.0141900510204),
      (250, 474.88679846938777),
      (260, 470.9527264030612),
      (270, 467.20336415816325),
      (280, 463.6305006377551),
      (290, 460.2261639030612),
      (300, 456.98286033163265),
      (310, 453.89337531887753),
      (320, 450.95089285714283),
      (330, 448.1488360969388),
      (340, 445.48054846938777),
      (350, 442.94029017857144),
      (360, 440.5222417091837),
      (370, 438.22094228316325),
      (380, 436.0308514030612),
      (390, 433.94698660714283),
      (400, 431.964524872449),
      (410, 430.07876275510205),
      (420, 428.28539540816325),
      (430, 426.5799585459184),
      (440, 424.9583864795918),
      (450, 423.41693239795916),
      (460, 421.9516900510204),
      (470, 420.5591517857143),
      (480, 419.2358498086735),
      (490, 417.9786352040816),
      (500, 416.78431919642856),
      (510, 415.6498724489796),
      (520, 414.5724649234694),
      (530, 413.5493064413265),
      (540, 412.5779655612245),
      (550, 411.65581154336735),
      (560, 410.78041294642856),
      (570, 409.9496970663265),
      (580, 409.16127232142856),
      (590, 408.4131855867347),
      (600, 407.703443877551),
      (610, 407.03021364795916),
      (620, 406.3915816326531),
      (630, 405.7858737244898),
      (640, 405.2114955357143),
      (650, 404.66693239795916),
      (660, 404.15062978316325),
      (670, 403.6611527423469),
      (680, 403.1971460459184),
      (690, 402.7574139030612),
      (700, 402.34064094387753),
      (710, 401.9457908163265),
      (720, 401.5715880102041),
      (730, 401.217115752551),
      (740, 400.88129783163265),
      (750, 400.56313775510205),
      (760, 400.26179846938777),
      (770, 399.9764429209184),
      (780, 399.70619419642856),
      (790, 399.45037468112247),
      (800, 399.2080277423469),
      (810, 398.97859534438777),
      (820, 398.76147959183675),
      (830, 398.55584343112247),
      (840, 398.36128826530614),
      (850, 398.17709661989795),
      (860, 398.0028300382653),
      (870, 397.83785076530614),
      (880, 397.6817602040816),
      (890, 397.53404017857144),
      (900, 397.39425223214283),
      (910, 397.262037627551),
      (920, 397.13699776785717),
      (930, 397.0185746173469),
      (940, 396.90668845663265),
      (950, 396.80078125),
      (960, 396.7006138392857),
      (970, 396.6059470663265),
      (980, 396.5163026147959),
      (990, 396.43156090561223),
    ).map(toXY)
  )



  val dataRow3 =    Viz.DataRow(
    name = Some("len:3"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 627.7449162679426),
      (4, 578.467591208134),
      (8, 515.3190789473684),
      (12, 468.770110645933),
      (16, 446.2122084330144),
      (20, 440.9599282296651),
      (24, 442.89600777511964),
      (28, 445.0809285287081),
      (32, 445.22659988038276),
      (36, 443.9257625598086),
      (40, 442.41791267942585),
      (44, 441.41895933014354),
      (48, 441.00904605263156),
      (52, 440.9567508971292),
      (56, 441.01779306220095),
      (60, 441.0586872009569),
      (64, 441.0503513755981),
      (68, 441.0144288277512),
      (72, 440.9785436602871),
      (76, 440.956115430622),
      (80, 440.9472936602871),
      (84, 440.9461722488038),
      (88, 440.94706937799043),
      (92, 440.9472936602871),
      (96, 440.9463591507177),
      (100, 440.94467703349284),
      (104, 440.9433313397129),
      (108, 440.941985645933),
      (112, 440.9414249401914),
      (116, 440.9407894736842),
      (120, 440.9405278110048),
      (124, 440.9399671052632),
      (128, 440.9392942583732),
      (132, 440.9385092703349),
      (136, 440.9379485645933),
      (140, 440.9373130980861),
      (144, 440.9368271531101),
      (148, 440.936004784689),
      (152, 440.93544407894734),
      (156, 440.9348086124402),
      (160, 440.93428528708137),
      (164, 440.9336872009569),
      (168, 440.9331638755981),
      (172, 440.9324910287081),
      (176, 440.9318181818182),
      (180, 440.9313322368421),
      (184, 440.93084629186603),
      (188, 440.93017344497605),
      (192, 440.92972488038276),
      (196, 440.9289398923445),
    ).map(toXY)
  )



  val dataRow4 =    Viz.DataRow(
    name = Some("len:4"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 382.41820580474933),
      (10, 317.2814973614776),
      (20, 294.4507397286091),
      (30, 296.4951941198643),
      (40, 295.20766584998114),
      (50, 294.4532604598568),
      (60, 294.4911892197512),
      (70, 294.4700103656238),
      (80, 294.4456511496419),
      (90, 294.4449444025631),
    ).map(toXY)
  )



  val dataRow5 =    Viz.DataRow(
    name = Some("len:5"),
    style = Viz.Style_LINES,
    data = Seq(
      (0, 222.90871021775544),
      (5, 218.006541717597),
      (10, 209.9007198152927),
      (15, 200.98626012947622),
      (20, 192.5858345782969),
      (25, 185.31365159128978),
      (30, 179.34035040065191),
      (35, 174.6069650052062),
      (40, 170.9540042555118),
      (45, 168.18923446059125),
      (50, 166.12970256688848),
      (55, 164.61381230476707),
      (60, 163.50964280863778),
      (65, 162.71252659694872),
      (70, 162.14120150301054),
      (75, 161.73328353479107),
      (80, 161.44535741771924),
      (85, 161.24136448005794),
      (90, 161.0986011136765),
      (95, 160.99825705102086),
    ).map(toXY)
  )



  {
    val dia = Viz.Diagram[Viz.XY](
      id = "scoreGrmRed01",
      title = "Score Grammar reduced",
      legendPlacement = Viz.LegendPlacement_RIGHT,
      //yRange = Some(Viz.Range(Some(14.0), Some(16.0))),
      dataRows = Seq(
        dataRow2,
        dataRow3,
        dataRow4,
        dataRow5
      )
    )
    Viz.createDiagram(dia)
  }

  def toXY(v: (Int, Double)): Viz.XY = Viz.XY(v._1, v._2)



}
