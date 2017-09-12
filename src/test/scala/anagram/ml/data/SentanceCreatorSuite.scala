package anagram.ml.data

import org.scalatest.{FunSuite, MustMatchers}

class SentanceCreatorSuite extends FunSuite with MustMatchers {

  val equalLenData = Seq(
    (Seq("a", "b", "c"), 3),
    (Seq("a"), 1),
    (Seq("c", "hallo"), 2),
  )

  for ((sent, len) <- equalLenData) {
    test(s"create sentances from equal length $sent") {
      val re = new SentanceCreator().slideSentances(sent, len)
      re.size mustBe 1
      re(0).size mustBe len
      re(0) mustBe sent
    }
  }

  val plusOneLenData = Seq(
    (Seq("a", "b", "c"), 2),
    (Seq("a", "b", "c", "d"), 3),
    (Seq("c", "hallo"), 1),
  )

  for ((sent, len) <- plusOneLenData) {
    test(s"create sentances from plus one length $sent") {
      val re = new SentanceCreator().slideSentances(sent, len)
      re.size  mustBe 2
      for (i <- 0 to 1) {
        re(i).size mustBe len
      }
    }
  }

  test(s"create sentances a b c d 2") {
    val re = new SentanceCreator().slideSentances(Seq("a", "b", "c", "d"), 2)
    re.size mustBe 3
    re(0) mustBe Seq("a", "b")
    re(1) mustBe Seq("b", "c")
    re(2) mustBe Seq("c", "d")
  }

}
