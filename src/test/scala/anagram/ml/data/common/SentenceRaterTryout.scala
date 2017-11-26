package anagram.ml.data.common

import anagram.model.plain.WordMapperFactoryPlain

object SentenceRaterTryout extends App {

  val mapper = WordMapperFactoryPlain.create
  val screa: SentenceCreator = new SentenceCreatorSliding()
  val splitter = new BookSplitterTxt
  val sentenceRater: SentenceRater = new SentenceRaterStraight(mapper)


  val bookResName = "books/CommonSense.txt"

  val plainSentences: Stream[Seq[String]] = splitter.splitSentences(bookResName)
  val sentences: Seq[Sentence] = screa.create(plainSentences, 4, mapper)
  val rated = sentenceRater.rateSentence(sentences)

  for ((r, i) <- rated.toSeq.sortBy(-_.rating).zipWithIndex) {
    val sentString = r.sentence.words.mkString(" ")
    println("%5d - %10.2f - %s" format(i, r.rating, sentString))
  }

}
