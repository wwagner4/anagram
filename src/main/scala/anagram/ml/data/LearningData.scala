package anagram.ml.data

import java.io.BufferedWriter
import java.util.Locale

import anagram.common.IoUtil
import org.slf4j.LoggerFactory

import scala.util.Random

case class Book(
                 filename: String,
                 title: String,
                 author: String,
               )

case class BookCollection(
                           id: String,
                           desc: String,
                           books: Seq[Book],
                           sentanceLength: Seq[Int],
                         )


object LearningData {

  private val log = LoggerFactory.getLogger("LearningData")
  val ran = new Random()
  val variance = 5

  val sentanceCreator: SentanceCreator = new SentanceCreatorSliding()


  def createData(bookCollection: BookCollection): Unit = {
    val wm: WordMapper = WordMap.createWordMapFromWordlistResource("wordlist/wordlist_small.txt")

    val uris = bookCollection.books.map(bc => IoUtil.uri(bc.filename))
    for (len <- bookCollection.sentanceLength) {
      val sent: Seq[Seq[String]] = sentanceCreator.create(uris, len, wm)
      val ldPath = IoUtil.saveDataToWorkDir(bookCollection.id, len, writeSentances(sent, wm)(_))
      log.info("created learning data in " + ldPath)
    }
    log.info("Created learning data for book collection:\n" + asString(bookCollection))
  }

  def writeSentances(sentances: Seq[Seq[String]], wm: WordMapper)(wr: BufferedWriter): Unit = {
    for (sent <- sentances) {
      for (rated <- polluteAndRateSentance(sent, wm)) {
        writeSentance(rated)(wr)
      }
    }
  }

  def writeSentance(sent: Seq[String])(wr: BufferedWriter): Unit = {
    wr.write(sent.mkString(";"))
    wr.write("\n")
  }

  def numExchange(sentSize: Int, rating: Int): Int = {
    require(sentSize >= 2)
    require(rating >= 0)
    require(rating <= 100)
    val re = (((100 - rating).toDouble / 100) * sentSize).toInt
    if (sentSize == 2 && rating < 100 && rating > 50) re + 1
    else if (sentSize == 3 && rating < 100 && rating > 30) re + 1
    else re
  }

  def exchange(sent: Seq[String], numEx: Int,wm: WordMapper): Seq[String] = {
    val idx = ran.shuffle(sent.indices.toList).take(numEx)
    for ((w, i) <- sent.zipWithIndex) yield {
      if (idx.contains(i)) wm.randomWord
      else w
    }
  }

  def polluteAndRateSentance(sent: Seq[String], wm: WordMapper): Seq[Seq[String]] = {
    val l = sent.length
    val ratings = (0 to (100, 100 / l)).toList
    ratings.flatMap(rate => Seq.fill(5) {
      val numEx =  numExchange(sent.size, rate)
      val sentEx: Seq[String] = exchange(sent, numEx, wm)
      val sentNum = sentEx.map(w => f(wm.toNum(w)))
      val ranRate = rate + ran.nextInt(variance * 2 + 1) - variance
      val sentRated: Seq[String] = sentNum :+ f(ranRate)
      sentRated
    })
  }

  def asString(bookCollection: BookCollection): String = {
    val sb = new StringBuilder
    sb.append("%30s: %s%n".format("Description", bookCollection.desc))
    for ((book, i) <- bookCollection.books.zipWithIndex) {
      sb.append("%30s: %-30s %-30s %s%n".format(s"Book #${i + 1}", book.title, book.author, book.filename))
    }
    sb.append("%30s: %s%n".format("ID", bookCollection.id))
    sb.append("%30s: %s%n".format("Sentance Lengths", bookCollection.sentanceLength.mkString(", ")))
    sb.toString()
  }

  def f(value: Int): String = "%d".formatLocal(Locale.ENGLISH, value)

}
