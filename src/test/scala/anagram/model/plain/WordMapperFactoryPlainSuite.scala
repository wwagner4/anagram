package anagram.model.plain

import anagram.words.Wordlists
import org.scalatest.{FunSuite, MustMatchers}

class WordMapperFactoryPlainSuite extends FunSuite with MustMatchers {

  private val wl = Wordlists.plain.wordList()
  private val wm = new WordMapperFactoryPlain(wl)

  val maxVowelData = Seq(
    ("assel", 'a'),
    ("asseel", 'e'),
    ("essal", 'a'),
    ("essil", 'e'),
    ("essiil", 'i'),
    ("wissol", 'i'),
    ("wissool", 'o'),
    ("wuuussool", 'u'),
    ("wuuussoool", 'o'),
    ("wuuussooolaaa", 'a'),
  )

  for((w, c) <- maxVowelData) {
    test(s"maxVowel $w $c") {
      wm.maxVowel(w) mustBe c
    }

  }

  val countCharData = Seq(
    ("asse", 'a', 1),
    ("asse", 's', 2),
    ("asse", 'e', 1),
    ("asse", 'f', 0),
  )

  for ((w, c, cnt) <- countCharData) {
    test(s"countChar $w $c") {
      wm.countChar(w, c) mustBe cnt
    }
  }
}
