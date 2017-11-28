package anagram.ml.data.analyze

import anagram.words.{Word, Wordlists}
import entelijan.viz.{Viz, VizCreatorGnuplot}

object AnalyzeFrequencyWordLen extends App {

  val _id = "fw_3k_Sig4_02a"
  val _title = "frequencies of words 30k sig(4)"
  val _wlFactory = Wordlists.plainFreq30k

  val _ff: Int => Double = ffSig(4)

  def ffIdenti(len: Int): Double = 1.0

  def ffLin(a: Double, k:Double)(len: Int): Double = a + k * len

  def ffSig(xOff: Double)(len: Int) = 1 / (1 + math.exp(xOff -len ))

  implicit val crea = VizCreatorGnuplot[Viz.X]()

  case class WordValue(word: Word, value: Double)

  def wordValue(word: Word, f: Int => Double) = WordValue(word, word.frequency.get * f(word.word.length))

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

  Viz.createDiagram(dia)

  for ((w, i) <- wvl.toSeq.sortBy(-_.value).zipWithIndex) {
    val word = w.word.word
    val value = w.value
    val freq = w.word.frequency.get
    println(f"$i%10d $value%10.4f $freq%10d $word")
  }

}
