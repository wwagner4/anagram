package anagram.ml.data.common

import java.io.BufferedWriter
import java.nio.file.Path
import java.util.Locale

import anagram.common.IoUtil
import anagram.common.IoUtil.{dirWork, save}
import anagram.model.CfgCreateData
import anagram.words.WordMapper
import org.slf4j.LoggerFactory


object CreateLearningData {

  private val log = LoggerFactory.getLogger("LearningData")

  val bookSplitter: BookSplitter = new BookSplitterTxt

  def createData(config: CfgCreateData): Unit = {

    val uris = config.bookCollection.books.map(bc => IoUtil.uri(bc.filename)).toStream
    for (len <- config.sentenceLength) {
      val split: Stream[Seq[String]] = uris.flatMap(bookSplitter.splitSentences)
      log.info(s"Found ${split.size} sentences in ${config.bookCollection.desc}")
      val sent: Seq[Sentence] = config.sentenceCreator.create(split, len, config.mapper)
      log.info(s"Created ${sent.size} sentences of length $len")
      val ldPath = saveDataToWorkDir(
        filePrefix(config.id, config.mapWordsToNumbers),
        len,
        writeSentences(len, sent, config)(_),
      )
      log.info("Created learning data in " + ldPath)
    }
    log.info("Created learning data for book collection:\n" + asString(config.id, config.bookCollection))
  }

  def filePrefix(id: String, mapWordsToNumber: Boolean): String = {
    if (mapWordsToNumber) id
    else s"${id}_unmapped"
  }

  def writeSentences(sentLength: Int, sentences: Seq[Sentence], config: CfgCreateData)(wr: BufferedWriter): Unit = {
    for (rated <- config.sentenceRater.rateSentence(sentences)) {
      val sentAdjusted = Sentence(
        rated.sentence.sentenceType,
        rated.sentence.words.map(word => if (config.mapWordsToNumbers) f(config.mapper.toNum(word)) else word)
      )
      val ratingAdjusted = config.adjustRating(rated.rating, sentLength)
      writeSentence(Rated(sentAdjusted, ratingAdjusted))(wr)
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

  private def saveDataToWorkDir(id: String, sentencelength: Int, f: BufferedWriter => Unit): Path = {
    saveTxtToWorkDir(s"${id}_data_$sentencelength", f)
  }

  private def saveTxtToWorkDir(id: String, f: BufferedWriter => Unit): Path = {
    val filename = s"anagram_$id.txt"
    save(dirWork, filename, f)
  }




}
