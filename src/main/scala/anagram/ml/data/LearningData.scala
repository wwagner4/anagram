package anagram.ml.data

import java.io.BufferedWriter
import java.util.Locale

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

  val booksEn01 = BookCollection(
    id = "en01",
    desc = "English Books Nr. 01",
    sentanceLength = 2 to 7,
    books = Seq(
      Book("books/ATaleofTwoCities.txt", "A Tale of Two Cities", "Charles Dickens"),
      Book("books/CommonSense.txt", "Common Sense", "Thomas Paine"),
      Book("books/StoriesbyEnglishAuthors.txt", "Stories by English Authors", "Various"),
      Book("books/TheAdventuresofTomSawyer.txt", "The Adventures of Tom Sawyer", "Mark Twain"),
      Book("books/ThePictureofDorianGray.txt", "The Picture of Dorian Gray", "Oscar Wilde"),
    )
  )

  val booksTwoLines = BookCollection(
    id = "twoLinesTest",
    desc = "Testset with one book containing only two lines",
    sentanceLength = 2 to 3,
    books = Seq(
      Book("books/TwoLines.txt", "TwoLines", "Test"),
    )
  )

  def createData(bookCollection: BookCollection): Unit = {
    val uris = bookCollection.books.map(bc => IoUtil.uri(bc.filename))
    val wm = WordMap.createWordMap(uris)
    val wmPath = IoUtil.saveTxtToWorkDir(s"${bookCollection.id}_map", wm.writeMap)
    log.info("created word map at workdir " + wmPath)
    for (len <- bookCollection.sentanceLength) {
      val sent = SentanceCreator.create(uris, len)
      val ldPath = IoUtil.saveTxtToWorkDir(s"${bookCollection.id}_data_$len", writeSentances(sent)(_))
      log.info("created learning data in " + ldPath)
    }
    log.info("Created learning data for book collection:\n" + asString(bookCollection))
  }

  def writeSentances(sentances: Stream[Seq[String]])(wr: BufferedWriter): Unit = {
    for (sent <- sentances) {
      for (rated <- polluteAndRateSentance(sent)) {
        writeSentance(rated)(wr)
      }
    }
  }

  def writeSentance(sent: Seq[String])(wr: BufferedWriter): Unit = {
    wr.write(sent.mkString(" "))
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

  def exchange(sent: Seq[String], numEx: Int): Seq[String] = {
    val idx = ran.shuffle(sent.indices.toList).take(numEx)
    for ((w, i) <- sent.zipWithIndex) yield {
      if (idx.contains(i)) "xxx"
      else w
    }
  }

  def polluteAndRateSentance(sent: Seq[String]): Seq[Seq[String]] = {
    val ratings = Seq(100, 75, 50, 25, 0)
    ratings.flatMap(r => Seq.fill(5) {
      val numEx =  numExchange(sent.size, r)
      val sentEx: Seq[String] = exchange(sent, numEx)
      val sentRated: Seq[String] = sentEx :+ "%5d".formatLocal(Locale.ENGLISH, r)
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

}
