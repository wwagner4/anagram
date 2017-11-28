package anagram.ml.data.analyze

import entelijan.viz.{Viz, VizCreator, VizCreatorGnuplot}

object DiaScore extends App {

  implicit val creator: VizCreator[Viz.XY] = VizCreatorGnuplot[Viz.XY]()

  val data2 = Seq(
    (0, 1.861871337890625),
    (200, 1.323674560546875),
    (400, 2.300814697265625),
    (600, 1.627994873046875),
    (800, 1.602137939453125),
    (1000, 1.8338809814453125),
    (1200, 1.0635556640625),
    (1400, 2.291283935546875),
    (1600, 1.360238525390625),
    (1800, 1.362999755859375),
    (2000, 1.5371646728515624),
    (2200, 1.3820416259765624),
    (2400, 1.689905029296875),
    (2600, 1.807233642578125),
    (2800, 1.6662490234375),
    (3000, 1.67637158203125),
    (3200, 0.827720947265625),
    (3400, 0.970235595703125),
    (3600, 0.8318779296875),
    (3800, 0.7348226928710937),
    (4000, 1.64784130859375),
    (4200, 1.2759808349609374),
    (4400, 1.79520166015625),
    (4600, 1.24876708984375),
    (4800, 0.81195703125),
    (5000, 0.8980819091796876),
    (5200, 1.5504658203125),
    (5400, 2.257098388671875),
    (5600, 1.299420654296875),
    (5800, 1.49149609375),
    (6000, 0.9225963134765625),
    (6200, 0.69669189453125),
    (6400, 1.715279052734375),
    (6600, 1.504487548828125),
    (6800, 1.383363525390625),
    (7000, 0.858420654296875),
    (7200, 0.8376002197265625),
    (7400, 1.0089208984375),
    (7600, 0.61392529296875),
    (7800, 0.31314215087890623),
    (8000, 1.3750118408203125),
  ).map(toXY)

  val data3 = Seq(
    (0, 1.2630928955078125),
    (200, 1.70704541015625),
    (400, 1.0286561279296875),
    (600, 1.2272227783203125),
    (800, 1.554392333984375),
    (1000, 0.7119783935546875),
    (1200, 0.8307686767578125),
    (1400, 1.035666748046875),
    (1600, 0.917668212890625),
    (1800, 1.7133990478515626),
    (2000, 1.58074853515625),
    (2200, 2.54652294921875),
    (2400, 0.6838165283203125),
    (2600, 1.302529541015625),
    (2800, 1.063702880859375),
    (3000, 0.7535499267578125),
    (3200, 1.071267578125),
    (3400, 0.7814628295898437),
    (3600, 1.138003662109375),
    (3800, 0.9176442260742188),
    (4000, 0.7470161743164062),
    (4200, 1.11878515625),
    (4400, 0.728742919921875),
    (4600, 0.5934423828125),
    (4800, 1.926732666015625),
    (5000, 1.887258056640625),
    (5200, 1.219963623046875),
    (5400, 0.7575512084960937),
    (5600, 0.7458111572265625),
    (5800, 1.827370849609375),
    (6000, 1.903515380859375),
    (6200, 1.0191136474609375),
    (6400, 0.596812255859375),
    (6600, 1.1062576904296875),
    (6800, 1.0017703857421876),
    (7000, 0.7130863647460938),
    (7200, 0.8633136596679688),
  ).map(toXY)

  val data4 = Seq(
    (0, 3.387050048828125),
    (200, 3.295643310546875),
    (400, 2.91760205078125),
    (600, 2.762656982421875),
    (800, 2.66618310546875),
    (1000, 2.336821044921875),
    (1200, 2.28338330078125),
    (1400, 2.0996181640625),
    (1600, 2.4064306640625),
    (1800, 1.8877520751953125),
    (2000, 1.967566650390625),
    (2200, 1.60946826171875),
    (2400, 2.055536376953125),
    (2600, 1.42600390625),
    (2800, 1.487174560546875),
    (3000, 1.378386962890625),
    (3200, 1.438348388671875),
    (3400, 1.547140380859375),
    (3600, 1.21925),
    (3800, 1.236624267578125),
    (4000, 1.097014404296875),
    (4200, 1.6706485595703124),
    (4400, 1.2248394775390625),
    (4600, 1.69959228515625),
    (4800, 1.217144287109375),
    (5000, 0.9885869140625),
    (5200, 1.2192840576171875),
    (5400, 1.4232584228515626),
    (5600, 0.9762763671875),
    (5800, 1.253922119140625),
    (6000, 1.541742919921875),
    (6200, 0.8724022216796875),
    (6400, 1.139160400390625),
  ).map(toXY)

  val data5 = Seq(
    (0, 1.1789359130859376),
    (200, 1.2113779296875),
    (400, 1.228635498046875),
    (600, 1.271307373046875),
    (800, 1.1642698974609376),
    (1000, 0.89380224609375),
    (1200, 0.8956749877929687),
    (1400, 0.949300048828125),
    (1600, 1.0786259765625),
    (1800, 1.69067724609375),
    (2000, 1.151662109375),
    (2200, 1.365216552734375),
    (2400, 0.8987313232421875),
    (2600, 0.8975187377929688),
    (2800, 0.7662737426757813),
    (3000, 1.3365272216796875),
    (3200, 1.07641943359375),
    (3400, 0.7743804931640625),
    (3600, 1.0338389892578126),
    (3800, 1.8453311767578124),
    (4000, 1.31330810546875),
    (4200, 0.8235776977539062),
    (4400, 0.798917236328125),
    (4600, 1.612793212890625),
    (4800, 1.0827681884765625),
    (5000, 0.7543968505859375),
    (5200, 0.875213623046875),
    (5400, 1.072280029296875),
    (5600, 1.14478271484375),
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


  val dia = Viz.Diagram[Viz.XY](
    id = "scorePlain01f",
    title = "ScorePlain bs: 500 lr: 1E-7",
    legendPlacement = Viz.LegendPlacement_RIGHT,
    dataRows = dataRows
  )

  Viz.createDiagram(dia)


}
