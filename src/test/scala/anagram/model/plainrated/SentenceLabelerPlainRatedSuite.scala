package anagram.model.plainrated

import anagram.ml.data.common.{Labeled, Sentence, SentenceLabeler, SentenceType_OTHER}
import anagram.words.Wordlists
import org.scalatest.{FunSuite, MustMatchers}

class SentenceLabelerPlainRatedSuite extends FunSuite with MustMatchers {

  private val wl = Wordlists.plainRatedLarge.wordList()
  private val wm = new WordMapperFactoryPlainRated(wl).create

  test("labels") {
    val sl: SentenceLabeler = new SentenceLabelerPlainRated(wm)
    val s = Seq(Sentence(SentenceType_OTHER, Seq("the", "better", "house")))
    val l: Seq[Labeled] = sl.labelSentence(s)

    l.size mustBe 1
    l(0).label mustBe 40.0 +- 0.0001
  }

}
