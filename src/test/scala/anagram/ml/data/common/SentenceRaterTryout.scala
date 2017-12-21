package anagram.ml.data.common

import anagram.model.plain.WordMapperFactoryPlain
import anagram.words.Wordlists

object SentenceRaterTryout extends App {

  private val wl = Wordlists.plain.wordList()
  val mapper = new WordMapperFactoryPlain(wl).create
  val screa: SentenceCreator = new SentenceCreatorSliding(mapper)
  val splitter = new BookSplitterTxt
  val sentenceRater: SentenceLabeler = new SentenceLabelerStraight(mapper)


  val bookResName = "books/CommonSense.txt"

  val plainSentences: Stream[Seq[String]] = splitter.splitSentences(bookResName)
  val sentences: Seq[Sentence] = screa.create(plainSentences, 4)
  val rated = sentenceRater.labelSentence(sentences)

  for ((r, i) <- rated.sortBy(-_.label).zipWithIndex) {
    val sentString = r.sentence.words.mkString(" ")
    println("%5d - %10.2f - %s" format(i, r.label, sentString))
  }

}
