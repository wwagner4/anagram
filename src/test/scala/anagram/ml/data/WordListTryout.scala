package anagram.ml.data

object WordListTryout extends App {

  val wl = WordList.loadWordList("wordlist/wordlist.txt")

  println(wl.mkString("\n"))

  val wlSorted : Iterable[String] = wl.toSeq.sorted.sortBy(_.length)





}
