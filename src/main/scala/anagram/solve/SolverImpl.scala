package anagram.solve

import anagram.words.Word

import scala.collection.GenIterable

case class SolverImpl(maxDepth: Int, parallel: Int) extends Solver {

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
              val mws1 = findMatchingWords(txt, words).filter(!_.isEmpty)
              val mws1Size = mws1.size
              val grpSize = if (mws1Size <= parallel) 1 else mws1Size / parallel
              mws1.grouped(grpSize).toSeq.par
            }
          //println(s"-- $depth :$txtSorted: - ${mws.mkString(" ")}")
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

  def validWordFromSorted(word: Word, txtSorted: String): Option[String] = {
    val lw = word.wordSorted.length
    val lt = txtSorted.length
    if (lw > lt) None
    else {
      def v(iw: Int, it: Int): Option[String] = {
        if (iw >= lw) {
          Some(word.word)
        }
        else if (it >= lt) {
          None
        }
        else {
          if (word.wordSorted(iw) == txtSorted(it)) v(iw+1, it+1)
          else v(iw, it+1)
        }
      }
      v(0, 0)
    }
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
    words.flatMap(w => validWordFromSorted(w, txt))
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
