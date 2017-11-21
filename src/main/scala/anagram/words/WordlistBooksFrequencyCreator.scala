package anagram.words

import anagram.common.IoUtil
import anagram.ml.data.common.{BookCollections, BookSplitterTxt}

object WordlistBooksFrequencyCreator extends App {


  val bc = BookCollections.collectionEn2

  val bs = new BookSplitterTxt


  val words: Seq[String] = bc.books.flatMap(book => bs.splitSentences(IoUtil.uri(book.filename))).flatten

  val wsorted = words.groupBy(identity)
    .map { case (w, l) => (w, l.size, w.length, lenFact(w.length)) }
    .toSeq
    .sortBy { case (_, l, _, wf) => -(l * wf) }
    .zipWithIndex

  def lenFact(len: Int): Double = 0.2 + 0.2 * len

  val lens = Seq(2000, 3000, 5000, 10000, 50000, 100000)

  for (len <- lens) {
    val outfileName = s"wordlist_books_frequency_$len.txt"
    val path = IoUtil.saveToWorkDir(outfileName, bw => {
      wsorted.take(len).foreach {
        case ((w, freq, _, _), _) => bw.write("%s;%s%n".format(w, freq))
      }
    })

    println(s"wrote to $path")
  }

}