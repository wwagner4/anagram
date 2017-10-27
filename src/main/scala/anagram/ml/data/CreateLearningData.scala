package anagram.ml.data

import java.io.BufferedWriter
import java.util.Locale

import anagram.common.IoUtil
import anagram.words.WordMapper
import org.slf4j.LoggerFactory

case class CreateDataConfig(
                             id: String,
                             bookCollection: BookCollection,
                             sentenceLength: Iterable[Int],
                             adjustRating: (Double, Int) => Double,
                           )


class CreateLearningData(wm: WordMapper, bookSplitter: BookSplitter, sentenceCreator: SentenceCreator, sentenceRater: SentenceRater, mapWordsToNumbers: Boolean = true) {

  private val log = LoggerFactory.getLogger("LearningData")

  def createData(config: CreateDataConfig): Unit = {

    val uris = config.bookCollection.books.map(bc => IoUtil.uri(bc.filename)).toStream
    for (len <- config.sentenceLength) {
      val split: Stream[Seq[String]] = uris.flatMap(bookSplitter.splitSentences)
      log.info(s"Found ${split.size} sentences in ${config.bookCollection.desc}")
      val sent: Seq[Sentence] = sentenceCreator.create(split, len, wm)
      log.info(s"Created ${sent.size} sentences of length $len")
      val ldPath = IoUtil.saveDataToWorkDir(config.id, len, writeSentences(len, sent, config.adjustRating)(_))
      log.info("Created learning data in " + ldPath)
    }
    log.info("Created learning data for book collection:\n" + asString(config.id, config.bookCollection))
  }

  def writeSentences(sentLength: Int, sentences: Seq[Sentence], adjRating: (Double, Int) => Double)(wr: BufferedWriter): Unit = {
    for (rated <- sentenceRater.rateSentence(sentences)) {
      val sentAdjusted = Sentence(
        rated.sentence.sentenceType,
        rated.sentence.words.map(word => if (mapWordsToNumbers) f(wm.toNum(word)) else word)
      )
      val ratingAdjusted = adjRating(rated.rating, sentLength)
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


}
