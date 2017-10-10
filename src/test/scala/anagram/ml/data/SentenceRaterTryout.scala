package anagram.ml.data

import anagram.common.IoUtil

object SentenceRaterTryout extends App {

  val wm = WordMap.createWordMapFromWordlistResource("wordlist/wordlist_small.txt")

  val sentence = Seq("ones", "upon", "time", "in", "a", "country", "far", "far", "away")

  val screa: SentenceCreator = new SentenceCreatorConditionalSliding()

  val sentences: Seq[Sentence] = createSentences1(3, screa)

  val sentenceRater: SentenceRater = new SentenceRaterAdapted(wm)

  val rated: Seq[Seq[Rated]] = for (s <- sentences) yield {
    sentenceRater.rateSentence(s)
  }

  for ((r, i) <- rated.flatten.sortBy(-_.rating).zipWithIndex) {
      val g = "%5d - %10.2f - %s" format(i, r.rating, r.sentence.words.mkString(" "))
      println(g)
  }

  private def createSentences1(sentLen: Int, _screa: SentenceCreator):Seq[Sentence] = {
    val books = Seq(IoUtil.uri("books/CommonSense.txt"))
    val s: Stream[Seq[String]] = BookSplitter.sentences(books);
    _screa.create(s, sentLen, wm)
  }


  private def createSentences: Seq[Sentence] = {
    (2 to 7).map(n => Sentence(SentenceType_OTHER, sentence.take(n)))
  }

}
