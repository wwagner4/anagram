package anagram.ml.data

object WordListTryout extends App {

  val wl = WordList.loadWordList(WordList.defaultWordlist)

  val grps = wl.grouped(20)
  println(grps.map(li => li.mkString(" ")).mkString("\n"))

}
