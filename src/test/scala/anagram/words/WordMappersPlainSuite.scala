package anagram.words

import anagram.ml.data.datamodel.plain.WordMappersPlain
import org.scalatest.{FunSuite, MustMatchers}

class WordMappersPlainSuite extends FunSuite with MustMatchers {

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
      WordMappersPlain.maxVowel(w) mustBe c
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
      WordMappersPlain.countChar(w, c) mustBe cnt
    }
  }
}
