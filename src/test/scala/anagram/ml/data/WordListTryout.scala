package anagram.ml.data

object WordListTryout extends App {

  val wl = WordList.loadWordList(WordList.defaultWordlist)

  println(wl.mkString("\n"))

  val wlSorted : Iterable[String] = wl.toSeq.sorted.sortBy(_.length)

  val p = WordList.saveWordListToWorkdir("test", wlSorted)

  println(s"Wrote sorted wordlist to $p")




}
