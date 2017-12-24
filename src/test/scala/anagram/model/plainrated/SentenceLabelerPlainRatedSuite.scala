package anagram.model.plainrated

import anagram.ml.data.common._
import anagram.words.Wordlists
import org.scalatest.{FunSuite, MustMatchers}

class SentenceLabelerPlainRatedSuite extends FunSuite with MustMatchers {

  private val wl = Wordlists.plainRatedLarge.wordList()
  private val wm = new WordMapperFactoryPlainRated(wl).create
  private val sl: SentenceLabeler = new SentenceLabelerPlainRated(wm)

  val dats = Seq(
    dat(3.3, SentenceType_OTHER, Seq("the", "better", "house")),
    dat(30.3, SentenceType_COMPLETE, Seq("the", "better", "house")),
    dat(15.3, SentenceType_BEGINNING, Seq("the", "better", "house")),
    dat(15.26, SentenceType_BEGINNING, Seq("the", "better", "trimming")),
  )

  for((l, s) <- dats) {
    test(s"labels ${s(0).sentenceType} ${s(0).words} ") {
      val l1: Seq[Labeled] = sl.labelSentence(s)
      l1.size mustBe 1
      l1(0).label mustBe l +- 0.01
    }

  }

  private def dat(label: Double, typ: SentenceType, words: Seq[String]): (Double, Seq[Sentence]) = {
    (label, Seq(Sentence(typ, words)))
  }

}
