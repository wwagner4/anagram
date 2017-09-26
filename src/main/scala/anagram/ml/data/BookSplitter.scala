package anagram.ml.data

import java.net.URI

import scala.io.Source


/**
  * Splits a list of books into sentences.
  */
object BookSplitter {

  val booksBig = Seq(
    "books/ATaleofTwoCities.txt",
    "books/CommonSense.txt",
    "books/StoriesbyEnglishAuthors.txt",
    "books/TheAdventuresofTomSawyer.txt",
    "books/ThePictureofDorianGray.txt",
  )

  val booksSmall = Seq(
    "books/TwoLines.txt",
  )

  private val validChars: Seq[Char] = (32 :: 46 :: (65 to 90).toList ::: (97 to 122).toList).map(i => i.toChar)

  def sentences(books: Seq[URI]): Stream[Seq[String]] = {
    books.toStream
      .flatMap(file =>
        Source.fromFile(file, "UTF-8").iter.toStream
          // convert all word separating characters to BLANK. (CR, LF, -)
          .map { case '\u000D' => ' ' case '\u000A' => ' ' case '-' => ' ' case any => any }
          .filter(validChars.contains(_))
          .map(_.toLower)
          .foldLeft(Stream.empty[List[Char]])(splitSentences)
          .map(_.mkString.split("\\s")
            .toList
            .filter(!_.isEmpty))
          .filter(_.size > 1)
      )
  }

  private def splitSentences(stream: Stream[List[Char]], char: Char): Stream[List[Char]] = stream match {
    case Stream.Empty => Stream(List(char))
    case head #:: rest if char == '.' => List.empty[Char] #:: head #:: rest
    case head #:: rest => (head :+ char) #:: rest
  }

}
