package anagram.words

object WordlistsTryout extends App {

  val wl = Wordlists.grammar

  wl.wordList().foreach(w => println("%s %s %s %s".format(w.word, w.wordSorted, w.grp, w.frequency)))

}
