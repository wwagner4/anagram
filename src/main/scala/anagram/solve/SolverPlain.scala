package anagram.solve

import anagram.words.Word
import org.slf4j.LoggerFactory

import scala.collection.GenIterable
import scala.collection.parallel.{ExecutionContextTaskSupport, ParIterable}
import scala.concurrent.ExecutionContext
import scala.util.Random

case class SolverPlain(maxDepth: Int, parallel: Int, words: Iterable[Word])(implicit ec: ExecutionContext) extends Solver {

  private var _cancelled = false

  private val log = LoggerFactory.getLogger("SolverPlain")

  override def solve(sourceText: String): Iterator[Iterable[String]] = {
    _cancelled = false
    val txtAdj = sourceText.toLowerCase().replaceAll("\\s", "").sorted
    solve1(txtAdj, 0, words.toList, new AnaCache()).toIterator
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
            if (depth >= 1) Seq(findMatchingWords(txt, words))
            else toParallel(findMatchingWords(txt, words))
          matchingWords.flatMap(_.flatMap { matchingWord =>
            val restTex = removeChars(txt, matchingWord.toList)
            val anagrams = solve1(restTex, depth + 1, words, anaCache)
            if (restTex.isEmpty && anagrams.isEmpty) List(List(matchingWord))
            else anagrams.map(sent => matchingWord :: sent)
          })
        }
      }
    anaCache.addAna(txt, re)
    re
  }

  def toParallel[T](in: Iterable[T]): ParIterable[Iterable[T]] = {
    val _size = in.size
    val grpSize = if (_size <= parallel) 1 else _size / parallel
    val out = in.grouped(grpSize).toSeq.par
    out.tasksupport = new ExecutionContextTaskSupport(ec)
    out
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
          if (word.wordSorted(iw) == txtSorted(it)) v(iw + 1, it + 1)
          else v(iw, it + 1)
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
    val mws: Seq[String] = words.flatMap(w => validWordFromSorted(w, txt))
    Random.shuffle(mws)
    mws.filter(!_.isEmpty)
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
