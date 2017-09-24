package anagram.ml.data

import anagram.common.IoUtil

object WordMapTryout extends App {

  val id = "tryout_twoLines"
  val uris = BookSplitter.booksSmall.map(IoUtil.uri)

  val wm = WordMap.createWordMapFromWordlistResource("wordlist/wordlist_small.txt")

  println(s"-- size:${wm.size}")
  println("-- following -> " + wm.toNum("following"))
  println("-- a -> " + wm.toNum("a"))

}
