package anagram.ml.data

import org.scalatest.{FunSuite, MustMatchers}

class SentanceCreatorSuite extends FunSuite with MustMatchers {

  private val wm = WordMap.createWordMapFromWordlistResource("wordlist/wordlist_test01.txt")

  val equalLenData = Seq(
    (Seq("at", "be", "come"), 3),
    (Seq("at"), 1),
    (Seq("come", "hallo"), 2),
  )

  for ((sent, len) <- equalLenData) {
    test(s"create sentances from equal length $sent") {
      val re = SentanceCreator.slideSentances(sent, len, wm)
      re.size mustBe 1
      re(0).size mustBe len
      re(0) mustBe sent
    }
  }

  val plusOneLenData = Seq(
    (Seq("at", "be", "come"), 2),
    (Seq("at", "be", "come", "do"), 3),
    (Seq("come", "hallo"), 1),
  )

  for ((sent, len) <- plusOneLenData) {
    test(s"create sentances from plus one length $sent") {
      val re = SentanceCreator.slideSentances(sent, len, wm)
      re.size  mustBe 2
      for (i <- 0 to 1) {
        re(i).size mustBe len
      }
    }
  }

  test(s"create sentances a b c d 2") {
    val re = SentanceCreator.slideSentances(Seq("at", "be", "come", "do"), 2, wm)
    re.size mustBe 3
    re(0) mustBe Seq("at", "be")
    re(1) mustBe Seq("be", "come")
    re(2) mustBe Seq("come", "do")
  }

}
