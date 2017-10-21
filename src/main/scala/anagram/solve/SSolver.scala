package anagram.solve

import anagram.ml.data.Word

import scala.collection.GenIterable

case class SSolver(maxDepth: Int) extends Solver {

  def solve(sourceText: String, words: Iterable[Word]): Stream[Ana] = {
    solve1(sourceText.toLowerCase().replaceAll("\\s", "").sorted, 0, words.toList, new AnaCache())
      .map(sent => Ana(1.0, sent))
      .toStream
  }

  def solve1(txt: String, depth: Int, words: List[Word], anaCache: AnaCache): GenIterable[List[String]] = {
    anaCache.ana(txt).getOrElse(solve2(txt, depth, words, anaCache))
  }

  def solve2(txt: String, depth: Int, words: List[Word], anaCache: AnaCache): GenIterable[List[String]] = {
    val re: GenIterable[List[String]] =
      if (txt.isEmpty) Iterable.empty[List[String]]
      else {
        if (depth >= maxDepth) Iterable.empty[List[String]]
        else {
          val mws =
            if (depth >= 1) Seq(findMatchingWords(txt, words).filter(!_.isEmpty))
            else {
              val parallel = 4
              val mws1 = findMatchingWords(txt, words).filter(!_.isEmpty)
              val mws1Size = mws1.size
              val grpSize = if (mws1Size <= parallel) 1 else mws1Size / parallel
              val grps = mws1.grouped(grpSize).toSeq
              val sizes = grps.map(_.size).mkString(", ")
              //println(s"-- parallel $depth - $mws1Size - $sizes")
              grps.par
            }
          //println(s"-- $depth :$txt: - ${mws.mkString(" ")}")
          mws.flatMap(_.flatMap { mw =>
            val restText = removeChars(txt, mw.toList)
            val subAnas = solve1(restText, depth + 1, words, anaCache)
            if (restText.isEmpty && subAnas.isEmpty) {
              List(List(mw))
            } else {
              subAnas.map(sent => mw :: sent)
            }
          })
        }
      }
    anaCache.addAna(txt, re)
    re
  }

  def validWord(word: Word, txt: String): Option[String] = {

    // TODO Optimize. txt and word sorted ???
    def vw(w: String, txt: String): Option[String] = {
      if (w.isEmpty) Some(word.word)
      else {
        val l = w.length
        val head = w.substring(0, 1)(0)
        val tail = w.substring(1, l)
        val i = txt.indexOf(head)
        if (i >= 0) vw(tail, removeFirst(head, txt, i))
        else None
      }
    }

    // println(s"validWord: $word - $txt")
    vw(word.word, txt)
  }

  def validWordFromSorted(word: String, txt: String): Option[String] = {

    // TODO Optimize. txt and word sorted ???
    def vw(w: String, txt: String): Option[String] = {
      if (w.isEmpty) Some(word)
      else {
        val l = w.length
        val head = w.substring(0, 1)(0)
        val tail = w.substring(1, l)
        val i = txt.indexOf(head)
        if (i >= 0) vw(tail, removeFirst(head, txt, i))
        else None
      }
    }

    // println(s"validWord: $word - $txt")
    vw(word, txt)
  }

  def removeFirst(c: Char, s: String, i: Int): String = {
    val l = s.length
    if (i == 0) {
      if (l == 1) ""
      else s.substring(1, l)
    }
    else {
      if (l == 1) ""
      else s.substring(0, i) + s.substring(i + 1, l)
    }
  }

  def removeFirst(c: Char, s: String): String = {
    val i = s.indexOf(c)
    if (i >= 0) removeFirst(c, s, i)
    else s
  }

  def findMatchingWords(txt: String, words: List[Word]): Iterable[String] = {
    words.flatMap(w => validWord(w, txt))
  }

  def removeChars(txt: String, mw: List[Char]): String = {
    mw match {
      case Nil => txt
      case c :: rest =>
        val txt1 = removeFirst(c, txt)
        removeChars(txt1, rest)
    }
  }

}
