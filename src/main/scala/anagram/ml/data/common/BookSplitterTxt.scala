package anagram.ml.data.common

import scala.io.Source

object BookSplitterTxt {

  val booksBig = Seq(
    "books/ATaleofTwoCities.txt",
    "books/CommonSense.txt",
    "books/StoriesbyEnglishAuthors.txt",
    "books/TheAdventuresofTomSawyer.txt",
    "books/ThePictureofDorianGray.txt",
  )

  val bookSmallRes = "books/TwoLines.txt"
  val bookCommonSenseRes = "books/CommonSense.txt"
}

  /**
  * Splits a list of books into sentences.
  */
class BookSplitterTxt extends BookSplitter {

  private val validChars: Seq[Char] = (32 :: 46 :: (65 to 90).toList ::: (97 to 122).toList).map(i => i.toChar)

  def splitSentences(resName: String): Stream[Seq[String]] = {
    val is = getClass.getClassLoader.getResourceAsStream(resName)
    Source.fromInputStream(is, "UTF-8").iter.toStream
      // convert all word separating characters to BLANK. (CR, LF, -)
      .map { case '\u000D' => ' ' case '\u000A' => ' ' case '-' => ' ' case any => any }
      .filter(validChars.contains(_))
      .map(_.toLower)
      .foldLeft(Stream.empty[List[Char]])(splitSentences)
      .map(_.mkString.split("\\s")
        .toList
        .filter(!_.isEmpty))
      .filter(_.lengthCompare(1) > 0)
  }

  private def splitSentences(stream: Stream[List[Char]], char: Char): Stream[List[Char]] = stream match {
    case Stream.Empty => Stream(List(char))
    case head #:: rest if char == '.' => List.empty[Char] #:: head #:: rest
    case head #:: rest => (head :+ char) #:: rest
  }

}
