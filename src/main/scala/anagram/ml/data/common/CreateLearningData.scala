package anagram.ml.data.common

import java.io.BufferedWriter
import java.util.Locale

import anagram.common.IoUtil.{dirWork, save}
import anagram.ml.MlUtil
import anagram.model.CfgCreateData
import org.slf4j.LoggerFactory


object CreateLearningData {

  private val log = LoggerFactory.getLogger("LearningData")

  val bookSplitter: BookSplitter = new BookSplitterTxt

  def createData(config: CfgCreateData): Unit = {
    log.info("Creating learning data from book collection:\n" + asString(config.bookCollection))
    val uris = config.bookCollection.books.map(bc => bc.filename).toStream
    for (len <- config.sentenceLengths) {
      val split: Stream[Seq[String]] = uris.flatMap(bookSplitter.splitSentences)
      log.info(s"Found ${split.size} sentences in ${config.bookCollection.desc}")
      val sent: Seq[Sentence] = config.sentenceCreator.create(split, len.length)
      log.info(s"Created ${sent.size} sentences of length $len")
      val labeled = config.sentenceLabeler.labelSentence(sent)
      log.info(s"Labeled ${labeled.size} sentences of length $len")
      val fn = MlUtil.dataFileName(config.id, len.id)
      val ldPath = save(
        dirWork,
        fn,
        writeLabeled(labeled, config)(_)
      )
      log.info("Wrote learning data to " + ldPath)
    }
  }

  def writeLabeled(labeled: Iterable[Labeled], config: CfgCreateData)(wr: BufferedWriter): Unit = {
    for (l <- labeled) {
      val line = l.features.map(f => formatNumber(f)) ++ Seq(formatNumber(l.label))
      wr.write(line.mkString(";"))
      wr.write("\n")
    }
  }

  def formatNumber(num: Number): String = "%f".formatLocal(Locale.ENGLISH, num.doubleValue())

  def asString(bookCollection: BookCollection): String = {
    val sb = new StringBuilder
    sb.append("%30s: %s%n".format("Description", bookCollection.desc))
    for ((book, i) <- bookCollection.books.zipWithIndex) {
      sb.append("%30s: %-30s %-30s %s%n".format(s"Book #${i + 1}", book.title, book.author, book.filename))
    }
    sb.toString()
  }

}
