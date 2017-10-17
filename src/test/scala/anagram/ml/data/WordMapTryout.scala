package anagram.ml.data

object WordMapTryout extends App {

  val id = "tryout_twoLines"

  val wm = WordMapSingleWord.createWordMapperFromWordlistResource("wordlist/wordlist_small.txt")

  println(s"-- size:${wm.size}")
  println("-- following -> " + wm.toNum("following"))
  println("-- a -> " + wm.toNum("a"))

}
