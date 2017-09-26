package anagram.ml.data

object SentenceRaterTryout extends App {

  val wm = WordMap.createWordMapFromWordlistResource("wordlist/wordlist_small.txt")

  val sentence = Seq(
    "ones",
    "upon",
    "time",
  )

  val sentenceRater: SentenceRater = new SentenceRaterSimple(wm, 0, false)

  val f = sentenceRater.rateSentence(sentence).map(_.mkString(" ")).mkString("\n")

  println(f)

}
