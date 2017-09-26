package anagram.ml.data

import java.io.BufferedWriter

import anagram.common.IoUtil
import org.slf4j.LoggerFactory

case class Book(
                 filename: String,
                 title: String,
                 author: String,
               )

case class BookCollection(
                           id: String,
                           desc: String,
                           books: Seq[Book],
                           sentenceLength: Seq[Int],
                         )


object LearningData {

  private val log = LoggerFactory.getLogger("LearningData")

  private val wm: WordMapper = WordMap.createWordMapFromWordlistResource("wordlist/wordlist_small.txt")
  private val variance = 5

  private val sentenceCreator: SentenceCreator = new SentenceCreatorSliding()
  private val sentenceRater: SentenceRater = new SentenceRaterSimple(wm, variance, true)

  def createData(bookCollection: BookCollection): Unit = {

    val uris = bookCollection.books.map(bc => IoUtil.uri(bc.filename))
    for (len <- bookCollection.sentenceLength) {
      val sent: Seq[Seq[String]] = sentenceCreator.create(uris, len, wm)
      val ldPath = IoUtil.saveDataToWorkDir(bookCollection.id, len, writeSentences(sent)(_))
      log.info("created learning data in " + ldPath)
    }
    log.info("Created learning data for book collection:\n" + asString(bookCollection))
  }

  def writeSentences(sentences: Seq[Seq[String]])(wr: BufferedWriter): Unit = {
    for (sent <- sentences) {
      for (rated <- sentenceRater.rateSentence(sent)) {
        writeSentence(rated)(wr)
      }
    }
  }

  def writeSentence(sent: Seq[String])(wr: BufferedWriter): Unit = {
    wr.write(sent.mkString(";"))
    wr.write("\n")
  }

  def asString(bookCollection: BookCollection): String = {
    val sb = new StringBuilder
    sb.append("%30s: %s%n".format("Description", bookCollection.desc))
    for ((book, i) <- bookCollection.books.zipWithIndex) {
      sb.append("%30s: %-30s %-30s %s%n".format(s"Book #${i + 1}", book.title, book.author, book.filename))
    }
    sb.append("%30s: %s%n".format("ID", bookCollection.id))
    sb.append("%30s: %s%n".format("Sentence Lengths", bookCollection.sentenceLength.mkString(", ")))
    sb.toString()
  }


}
