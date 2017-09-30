package anagram.ml.data

object SentenceRaterTryout extends App {

  val wm = WordMap.createWordMapFromWordlistResource("wordlist/wordlist_small.txt")

  val sentence = Seq("ones", "upon", "time", "in", "a", "country", "far", "far", "away")

  val sentences = (2 to 7).map(sentence.take)

  val sentenceRater: SentenceRater = new SentenceRaterAdapted(wm)

  for (s <- sentences) {
    for (r <- sentenceRater.rateSentence(s)) {
      val g = "%10.2f - %s" format(r.rating, r.sentence.mkString(" "))
      println(g)
    }
  }

}
