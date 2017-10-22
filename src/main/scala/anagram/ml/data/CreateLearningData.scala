package anagram.ml.data

import java.io.BufferedWriter
import java.util.Locale

import anagram.common.IoUtil
import org.slf4j.LoggerFactory

case class CreateDataConfig(
                             id: String,
                             bookCollection: BookCollection,
                             sentenceLength: Iterable[Int],
                           )


class CreateLearningData(wm: WordMapper, bookSplitter: BookSplitter, sentenceCreator: SentenceCreator, sentenceRater: SentenceRater, mapWordsToNumbers: Boolean = true) {

  private val log = LoggerFactory.getLogger("LearningData")

  private val variance = 0

  private val ran = new util.Random()

  def createData(config: CreateDataConfig): Unit = {

    val uris = config.bookCollection.books.map(bc => IoUtil.uri(bc.filename)).toStream
    for (len <- config.sentenceLength) {
      val split: Stream[Seq[String]] = uris.flatMap(bookSplitter.splitSentences)
      log.info(s"Found ${split.size} sentences in ${config.bookCollection.desc}")
      val sent: Seq[Sentence] = sentenceCreator.create(split, len, wm)
      log.info(s"Created ${sent.size} sentences of length $len")
      val ldPath = IoUtil.saveDataToWorkDir(config.id, len, writeSentences(sent)(_))
      log.info("Created learning data in " + ldPath)
    }
    log.info("Created learning data for book collection:\n" + asString(config.id, config.bookCollection))
  }

  def writeSentences(sentences: Seq[Sentence])(wr: BufferedWriter): Unit = {
    for (rated <- sentenceRater.rateSentence(sentences)) {
      val ranRate = rated.rating + (ran.nextInt(variance * 2 + 1) - variance)
      val numSent = Sentence(
        rated.sentence.sentenceType,
        rated.sentence.words.map(word => if (mapWordsToNumbers) f(wm.toNum(word)) else word)
      )
      val numRated = Rated(numSent, ranRate)
      writeSentence(numRated)(wr)
    }
  }

  def writeSentence(rated: Rated)(wr: BufferedWriter): Unit = {
    val line = rated.sentence.words ++ Seq(f(rated.rating))
    wr.write(line.mkString(";"))
    wr.write("\n")
  }

  def f(value: Number): String = "%f".formatLocal(Locale.ENGLISH, value.doubleValue())

  def asString(id: String, bookCollection: BookCollection): String = {
    val sb = new StringBuilder
    sb.append("%30s: %s%n".format("Description", bookCollection.desc))
    for ((book, i) <- bookCollection.books.zipWithIndex) {
      sb.append("%30s: %-30s %-30s %s%n".format(s"Book #${i + 1}", book.title, book.author, book.filename))
    }
    sb.append("%30s: %s%n".format("ID", id))
    sb.toString()
  }


}
