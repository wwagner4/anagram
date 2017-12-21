package anagram.ml.data.common

import anagram.model.plain.WordMapperFactoryPlain
import anagram.words.Wordlists
import org.scalatest.{FunSuite, MustMatchers}

class SentenceCreatorSuite extends FunSuite with MustMatchers {

  private val wl = Wordlists.plain.wordList()
  private val wm = new WordMapperFactoryPlain(wl).create
  private val scSliding = new SentenceCreatorSliding(wm)

  val equalLenData = Seq(
    (Seq("at", "be", "come"), 3),
    (Seq("at"), 1),
    (Seq("come", "hallo"), 2),
  )

  for ((sent, len) <- equalLenData) {
    test(s"create sentences from equal length $sent") {
      val re = scSliding.slideSentences(sent, len)
      re.size mustBe 1
      re(0).words.size mustBe len
      re(0).words mustBe sent
    }
  }

  val plusOneLenData = Seq(
    (Seq("at", "be", "come"), 2),
    (Seq("at", "be", "come", "do"), 3),
    (Seq("come", "hallo"), 1),
  )

  for ((sent, len) <- plusOneLenData) {
    test(s"create sentences from plus one length $sent") {
      val re = scSliding.slideSentences(sent, len)
      re.size  mustBe 2
      for (i <- 0 to 1) {
        re(i).words.size mustBe len
      }
    }
  }

  test(s"create sentences a b c d 2") {
    val re = scSliding.slideSentences(Seq("at", "be", "come", "do"), 2)
    re.size mustBe 3
    re(0).words mustBe Seq("at", "be")
    re(1).words mustBe Seq("be", "come")
    re(2).words mustBe Seq("come", "do")
  }

}
