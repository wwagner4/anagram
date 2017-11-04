package anagram.solve

import anagram.words.Word

import scala.collection.GenIterable
import scala.collection.parallel.ExecutionContextTaskSupport
import scala.concurrent.ExecutionContext

case class SolverPlain(maxDepth: Int, parallel: Int)(implicit ec: ExecutionContext) extends Solver {

  private var _cancelled = false

  override def solve(sourceText: String, words: Iterable[Word]): Iterator[Ana] = {
    _cancelled = false
    solve1(sourceText.toLowerCase().replaceAll("\\s", "").sorted, 0, words.toList, new AnaCache())
      .map(sent => Ana(1.0, sent))
      .toIterator
  }

  override def cancel(): Unit = _cancelled = true

  override def toString: String = s"Plain MaxDepth:$maxDepth paralell:$parallel"

  def solve1(txt: String, depth: Int, words: List[Word], anaCache: AnaCache): GenIterable[List[String]] = {
    if (_cancelled) Iterable.empty[List[String]]
    else anaCache.ana(txt).getOrElse(solve2(txt, depth, words, anaCache))
  }

  def solve2(txt: String, depth: Int, words: List[Word], anaCache: AnaCache): GenIterable[List[String]] = {
    val re: GenIterable[List[String]] =
      if (txt.isEmpty) Iterable.empty[List[String]]
      else {
        if (depth >= maxDepth) Iterable.empty[List[String]]
        else {
          val matchingWords =
            if (depth >= 1) {
              Seq(findMatchingWords(txt, words).filter(!_.isEmpty))
            }
            else {
              val mws1 = findMatchingWords(txt, words).filter(!_.isEmpty)
              val mws1Size = mws1.size
              val grpSize = if (mws1Size <= parallel) 1 else mws1Size / parallel
              val mwsp = mws1.grouped(grpSize).toSeq.par
              val ts = new ExecutionContextTaskSupport(ec)
              mwsp.tasksupport = ts
              mwsp
            }
          //println(s"-- $depth :$txtSorted: - ${matchingWords.mkString(" ")}")
          matchingWords.flatMap(_.flatMap { mw =>
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
