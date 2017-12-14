package anagram.ml.data.analyze

import anagram.common.IoUtil
import anagram.words.{Word, Wordlists}
import entelijan.viz.{Viz, VizCreatorGnuplot}

object AnalyzeFrequencyWordLen extends App {

  implicit val creaX = VizCreatorGnuplot[Viz.X]()
  implicit val creaXY = VizCreatorGnuplot[Viz.XY]()

  val _id = "fw_3k_Sig4_02a"
  val _title = "frequencies of words 30k sig(4)"
  val _wlFactory = Wordlists.plainFreq30k

  val _ff: Int => Double = ffSig(4)

  def ffIdenti(len: Int): Double = 1.0

  def ffLin(a: Double, k: Double)(len: Int): Double = a + k * len

  def ffSig(xOff: Double)(len: Int) = 1 / (1 + math.exp(xOff - len))

  case class WordValue(word: Word, value: Double)

  def wordValue(word: Word, f: Int => Double) = WordValue(word, word.rating.get * f(word.word.length))

  val wvl: Iterable[WordValue] = _wlFactory.wordList().map(wordValue(_, _ff))
  val wlen = wvl.groupBy(w => w.word.word.length)
    .filter(_._1 < 9)
    .toSeq
    .sortBy(_._1)

  def toData(w: Iterable[WordValue]): Seq[Viz.X] = w.toSeq.map(wv => Viz.X(wv.value))

  val dataRows = for ((l, w) <- wlen) yield {
    Viz.DataRow(
      style = Viz.Style_BOXPLOT,
      name = Some(s"$l"),
      data = toData(w),
    )
  }

  val dia = Viz.Diagram(
    _id,
    _title,
    yRange = Some(Viz.Range(Some(0), Some(500))),
    dataRows = dataRows,
  )


  def adjustValue(n: Int, max: Double)(value: Double, index: Int): Int = {
    (max - max * index / n).toInt
  }

  var wrDataRows = List.empty[Viz.XY]
  val file = IoUtil.save(IoUtil.dirOut, "wordlist_rated_19000.txt", { bw =>
    val a = adjustValue(wvl.size, 100)(_, _)
    for ((w, i) <- wvl.toSeq.sortBy(-_.value).zipWithIndex) {
      val word = w.word.word
      val value = w.value
      val v1 = a(value, i)
      val intvalue = (w.value * 1000).toInt
      val freq = w.word.rating.get
      println(f"$i%10d $value%10.3f $v1%10.3f $freq%10f.3 $word")
      if (value >= 1.0) bw.write(f"$word;$v1\n")

      if (i % 10 == 0) {
        wrDataRows = Viz.XY(i, v1) :: wrDataRows
      }
    }
  })
  println(s"Wrote rated wordlist to $file")

  val dia1 = Viz.Diagram(
    "wr",
    "word ratings",
    //yRange = Some(Viz.Range(Some(0.0), Some(5))),
    dataRows = Seq(
      Viz.DataRow(Some("values int"), Viz.Style_LINES, wrDataRows),
    )
  )
  Viz.createDiagram(dia1)

}
