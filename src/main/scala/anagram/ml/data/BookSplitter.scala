package anagram.ml.data

import java.io.File

import scala.io.Source

case class Book(
                 filename: String,
                 title: String,
                 author: String,
               )

object BookSplitter {

  private val validChars: Seq[Char] = (32 :: 46 :: (65 to 90).toList ::: (97 to 122).toList).map(i => i.toChar)

  val booksCommonSense = Seq(
    Book("CommonSense.txt", "Common Sense", "Thomas Paine"),
  )

  val booksTwoLines = Seq(
    Book("TwoLines.txt", "TwoLines", "Test"),
  )

  val books = Seq(
    Book("ATaleofTwoCities.txt", "A Tale of Two Cities", "Charles Dickens"),
    Book("CommonSense.txt", "Common Sense", "Thomas Paine"),
    Book("StoriesbyEnglishAuthors.txt", "Stories by English Authors", "Various"),
    Book("TheAdventuresofTomSawyer.txt", "The Adventures of Tom Sawyer", "Mark Twain"),
    Book("ThePictureofDorianGray.txt", "The Picture of Dorian Gray", "Oscar Wilde"),
  )

  def sentances(books: Seq[Book]): Stream[Seq[String]] = {
    books.toStream
      .map(bookToFile)
      .flatMap(file =>
        Source.fromFile(file, "UTF-8").iter.toStream
          .map { case '\u000D' => ' ' case '\u000A' => ' ' case '-' => ' ' case any => any }
          .filter(BookSplitter.validChars.contains(_))
          .map(_.toLower)
          .foldLeft(Stream.empty[List[Char]])(splitSentances)
          .map(_.mkString.split("\\s")
            .toList
            .filter(!_.isEmpty))
          .filter(_.size > 1)
      )
  }

  private def bookToFile(book: Book): File = {
    val resName = s"books/${book.filename}"
    val uri = getClass.getClassLoader.getResource(resName).toURI
    new File(uri)
  }

  private def splitSentances(stream: Stream[List[Char]], char: Char): Stream[List[Char]] = stream match {
    case Stream.Empty => Stream(List(char))
    case head #:: rest if char == '.' => List.empty[Char] #:: head #:: rest
    case head #:: rest => (head :+ char) #:: rest
  }

}
