package anagram.ml.data.analyze

import anagram.ml.data.common.{BookCollections, BookSplitterTxt}

object WordCounter extends App {

  val coll = BookCollections.collectionEn2
  val splitter = new BookSplitterTxt

  val x: Set[String] = coll.books
    .map(_.filename)
    .flatMap(resName => splitter.splitSentences(resName))
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
