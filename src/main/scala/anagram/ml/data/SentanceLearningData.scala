package anagram.ml.data

import java.io.BufferedWriter

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


object SentanceLearningData {

  val booksEn01 = BookCollection(
    id = "en01",
    desc = "English Books Nr. 01",
    sentanceLength = 2 to 5,
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
    val (is, si) = WordMap.createWordMap(uris)
    IoUtil.saveTxtToWorkDir(bookCollection.id, WordMap.writeMap(si)(_))
    for (len <- bookCollection.sentanceLength) {
      val sent = SentanceCreator.create(uris, len)
      IoUtil.saveTxtToWorkDir(s"${bookCollection.id}_$len", writeSentances(sent)(_))
    }
  }

  def writeSentances(sentances: Stream[Seq[String]])(wr: BufferedWriter): Unit = {
    sentances.foreach(sent => writeSentance(sent)(wr))
  }

  def writeSentance(sent: Seq[String])(wr: BufferedWriter): Unit = {
    wr.write(sent.mkString(" "))
    wr.write("\n")
  }

}
