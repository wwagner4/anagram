package anagram.ml.data.analyze

import anagram.common.IoUtil
import anagram.ml.data.{BookCollections, BookSplitterTxt}

object WordCounter extends App {

  val coll = BookCollections.collectionEn2
  val splitter = new BookSplitterTxt

  val x: Set[String] = coll.books
    .map(_.filename)
    .flatMap(resName => splitter.splitSentences(IoUtil.uri(resName)))
    .flatten
    .groupBy(identity)
    .toList
    .filter(_._1.length > 2)
    .map { case (w, ws) => (w, ws.size) }
    .sortBy(-_._2)
    .map(_._1)
    .take(2000)
    .toSet

  println(x.mkString("\n"))


}
