package anagram.ml.data

import java.nio.file.Paths

import anagram.common.IoUtil

object WordListTryout extends App {

  val wl = WordList.loadWordList(Paths.get(IoUtil.uri("wordlist/wordlist.txt")))

  println(wl.mkString("\n"))

  val wlSorted : Iterable[String] = wl.toSeq.sorted.sortBy(_.length)

  val p = WordList.saveWordListToWorkdir("test", wlSorted)

  println(s"Wrote sorted wordlist to $p")




}
