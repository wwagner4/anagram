package anagram.ml.data.analyze

import anagram.common.IoUtil
import anagram.words.{Word, Wordlists}
import entelijan.viz.{Viz, VizCreator, VizCreatorGnuplot}

object AnalyzeFrequencyWordLen extends App {

  implicit val creaX: VizCreator[Viz.X] = VizCreatorGnuplot[Viz.X]()
  implicit val creaXY: VizCreator[Viz.XY] = VizCreatorGnuplot[Viz.XY]()

  val _id = "fw_3k_Sig4_02a"
  val _title = "frequencies of words 30k sig(4)"
  val _wordListFactory = Wordlists.plainFreq30k

  // Defines which of the 'ff' functions is used to calculate the word value
  val _ff: Word => Double = ffSig(5)

  def ffIdenti(w: Word): Double = w.rating.get

  def ffLin(a: Double, k: Double)(w: Word): Double = w.rating.get * (a + k * w.word.length)

  def ffSig(xOff: Double)(w: Word) = w.rating.get * (1 / (1 + math.exp(xOff - w.word.length)))

  case class WordValue(word: Word, value: Double)

  def wordValue(word: Word, f: Word => Double) = WordValue(word, f(word))

  val wvl: Iterable[WordValue] = _wordListFactory.wordList().map(wordValue(_, _ff))
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
    (0.5 + (max - max * index / n)).toInt
  }

  var wrDataRows = List.empty[Viz.XY]
  val file = IoUtil.save(IoUtil.dirOut, "wordlist_rated_large_fine.txt", { bw =>
    val filtered = wvl.toSeq.sortBy(-_.value).zipWithIndex.filter(_._2 <= 27706)
    val a = adjustValue(filtered.size, 1000)(_, _)
    for ((w, i) <- filtered) {
      val word = w.word.word
      val value = w.value
      val v1 = a(value, i)
      val freq = w.word.rating.get
      println(f"$i%10d $value%10.3f $v1%10d $freq%10.3f - $word")
      bw.write(f"$word;$v1\n")

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
