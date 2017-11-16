package anagram.ml.data.wordlist

import anagram.common.IoUtil
import anagram.ml.data.{BookCollections, BookSplitterTxt}

object WordlistBooksFrequency extends App {

  val outfileName = "wordlist_books_frequency.txt"

  val bc = BookCollections.collectionEn2

  val bs = new BookSplitterTxt


  val words: Seq[String] = bc.books.flatMap(book => bs.splitSentences(IoUtil.uri(book.filename))).flatten

  val wsorted = words.groupBy(identity)
    .map { case (w, l) => (w, l.size, w.length, lenFact(w.length)) }
    .toSeq
    .sortBy{case (_, l, _, wf) => -(l * wf)}
    .zipWithIndex

  def lenFact(len: Int): Double = 0.2 + 0.2 * len

  wsorted.foreach { case ((w, freq, wl, wf), i) => println("%5d %20s - %10d %.3f".format(i, w, freq, wf)) }

}
