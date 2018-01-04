package anagram.model.grm

import anagram.words.Wordlists
import org.scalatest.{FunSuite, MustMatchers}

class WordMapperFactoryGrammarSuite extends FunSuite with MustMatchers {

  private lazy val wl = Wordlists.grammar.wordList()
  private lazy val grp = new GrouperGrm(wl)

  test("transform house") {
    grp.group("house") mustBe Seq("vi")
  }

  test("transform table") {
    grp.group("table") mustBe Seq("vi")
  }

  test("transform eat") {
    grp.group("eat") mustBe Seq("vi")
  }

  test("transform i") {
    grp.group("i") mustBe Seq("pron")
  }

  test("transform yammiyammi") {
    grp.group("yammiyammi") mustBe Seq("?")
  }
}
