package anagram.ml.data.common

import java.io.BufferedWriter
import java.nio.file.Path
import java.util.Locale

import anagram.common.IoUtil.{dirWork, save}
import anagram.ml.MlUtil
import anagram.model.{CfgCreateData, SentenceLength}
import org.slf4j.LoggerFactory


object CreateLearningData {

  private val log = LoggerFactory.getLogger("LearningData")

  val bookSplitter: BookSplitter = new BookSplitterTxt

  def createData(config: CfgCreateData[_]): Unit = {
    val uris = config.bookCollection.books.map(bc => bc.filename).toStream
    for (len <- config.sentenceLengths) {
      val cid = s"${config.id} - ${len.desc}"
      log.info(s"Creating Data for $cid")
      val split: Stream[Seq[String]] = uris.flatMap(bookSplitter.splitSentences)
      log.info(s"Found ${split.size} sentences in ${config.bookCollection.desc}")
      val sent: Seq[Sentence] = config.sentenceCreator.create(split, len.length)
      log.info(s"Created ${sent.size} sentences of length ${len.length}")
      val labeled = config.sentenceLabeler.labelSentence(sent)
      log.info(s"Labeled ${sent.size} sentences of length ${len.length}")
      val ldPath = saveDataToWorkDir(
        config.id,
        len,
        writeSentences(labeled, config)(_)
      )
      log.info(s"Created learning data for $cid in $ldPath")
    }
    log.info("Created learning data for book collection:\n" + asString(config.id, config.bookCollection))
  }

  def saveDataToWorkDir(id: String, sl: SentenceLength, f: BufferedWriter => Unit): Path = {
    val filename = MlUtil.dataFileName(id, sl.id)
    save(dirWork, filename, f)
  }

  def writeSentences(labeled: Seq[Labeled], config: CfgCreateData[_])(wr: BufferedWriter): Unit = {
    for (l <- labeled) {
      val line = l.features ++ Seq(formatNumber(l.label))
      wr.write(line.mkString(";"))
      wr.write("\n")
    }
  }

  def formatNumber(num: Number): String = "%f".formatLocal(Locale.ENGLISH, num.doubleValue())

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
