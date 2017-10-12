package anagram.ml.data

import anagram.common.IoUtil

object SentenceRaterTryout extends App {

  val wm = WordMap.createWordMapFromWordlistResource("wordlist/wordlist_small.txt")
  val screa: SentenceCreator = new SentenceCreatorSliding()
  val splitter = new BookSplitterTxt
  val sentenceRater: SentenceRater = new SentenceRaterStraight(wm)


  val bookUri = IoUtil.uri("books/CommonSense.txt")

  val s: Stream[Seq[String]] = splitter.splitSentences(bookUri);
  val rated = screa.create(s, 4, wm).flatMap(sentenceRater.rateSentence(_))

  for ((r, i) <- rated.sortBy(-_.rating).zipWithIndex) {
    val sentString = r.sentence.words.mkString(" ")
    println("%5d - %10.2f - %s" format(i, r.rating, sentString))
  }


}
