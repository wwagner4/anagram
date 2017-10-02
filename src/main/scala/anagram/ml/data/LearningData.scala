package anagram.ml.data

import java.io.BufferedWriter
import java.util.Locale

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


class LearningData(wm: WordMapper, sentenceCreator: SentenceCreator) {

  private val log = LoggerFactory.getLogger("LearningData")

  private val variance = 0

  private val sentenceRater: SentenceRater = new SentenceRaterAdapted(wm)
  private val ran = new util.Random()

  def createData(bookCollection: BookCollection): Unit = {

    val uris = bookCollection.books.map(bc => IoUtil.uri(bc.filename))
    for (len <- bookCollection.sentenceLength) {
      val sent: Seq[Sentence] = sentenceCreator.create(uris, len, wm)
      val ldPath = IoUtil.saveDataToWorkDir(bookCollection.id, len, writeSentences(sent)(_))
      log.info("created learning data in " + ldPath)
    }
    log.info("Created learning data for book collection:\n" + asString(bookCollection))
  }

  def writeSentences(sentences: Seq[Sentence])(wr: BufferedWriter): Unit = {
    for (sent <- sentences) {
      for (rated <- sentenceRater.rateSentence(sent)) {
        val ranRate = rated.rating + (ran.nextInt(variance * 2 + 1) - variance)
        val numSent = Sentence(
          rated.sentence.sentenceType,
          rated.sentence.words.map(word => f(wm.toNum(word)))
        )
        val numRated = Rated(numSent, ranRate)
        writeSentence(numRated)(wr)
      }
    }
  }

  def writeSentence(rated: Rated)(wr: BufferedWriter): Unit = {
    val line = rated.sentence.words ++ Seq(f(rated.rating))
    wr.write(line.mkString(";"))
    wr.write("\n")
  }

  def f(value: Number): String = "%f".formatLocal(Locale.ENGLISH, value.doubleValue())

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
