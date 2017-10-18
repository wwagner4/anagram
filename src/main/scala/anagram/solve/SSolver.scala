package anagram.solve

import scala.collection.GenIterable

case class SSolver(maxDepth: Int) extends Solver {

  def solve(sourceText: String, words: Iterable[String]): Stream[Ana] = {
    solve1(sourceText.toLowerCase().replaceAll("\\s", "").sorted, 0, words.toList, new AnaCache())
      .map(sent => Ana(1.0, sent))
      .toStream
  }

  def solve1(txt: String, depth: Int, words: List[String], anaCache: AnaCache): GenIterable[List[String]] = {
    anaCache.ana(txt).getOrElse(solve2(txt, depth, words, anaCache))
  }

  def solve2(txt: String, depth: Int, words: List[String], anaCache: AnaCache): GenIterable[List[String]] = {
    val re: GenIterable[List[String]] =
      if (txt.isEmpty) Iterable.empty[List[String]]
      else {
        if (depth >= maxDepth) Iterable.empty[List[String]]
        else {
          val mws = if (depth >= 1) findMatchingWords(txt, words).filter(!_.isEmpty)
          else findMatchingWords(txt, words).filter(!_.isEmpty).par
          //println(s"-- $depth :$txt: - ${mws.mkString(" ")}")
          mws.flatMap { mw =>
            val restText = removeChars(txt, mw.toList)
            val subAnas = solve1(restText, depth + 1, words, anaCache)
            if (restText.isEmpty && subAnas.isEmpty) {
              List(List(mw))
            } else {
              subAnas.map(sent => mw :: sent)
            }
          }
        }
      }
    anaCache.addAna(txt, re)
    re
  }

  def validWord(word: String, txt: String): Option[String] = {

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

    println(s"validWord: $word - $txt")
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

  def findMatchingWords(txt: String, words: List[String]): Iterable[String] = {
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
