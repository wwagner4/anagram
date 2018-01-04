package anagram.model.plainrated

import anagram.words.Word
import org.scalatest.{FunSuite, MustMatchers}

//noinspection ComparingLength
class WordMapperFactoryPlainRatedSuite extends FunSuite with MustMatchers {

  def word(str: String, rating: Double): Word = {
    Word(str, str.sorted, None, Some(rating))
  }

  val wl = Seq(
    word("a", 1.5),
    word("b", 2.5),
    word("c", 2.7),
    word("d", 5.0),
  )

  val wm  = new WordMapperFactoryPlainRated(wl).create

  test("Number of features and intermediate") {
    val mr = wm.map(Seq("a", "b", "c"))

    mr.intermediate.size equals 3
    mr.features.size equals 6
  }

  test("Values of intermediate") {
    val mr = wm.map(Seq("a", "b", "c"))
    mr.intermediate(0) equals 1.5 +- 0.0001
    mr.intermediate(1) equals 2.5 +- 0.0001
    mr.intermediate(2) equals 2.7 +- 0.0001
  }

  test("Values of features") {
    val mr = wm.map(Seq("a", "b", "c"))
    mr.features(0) equals 0.0 +- 0.0001
    mr.features(1) equals 1.5 +- 0.0001
    mr.features(2) equals 2.0 +- 0.0001
    mr.features(3) equals 2.5 +- 0.0001
    mr.features(4) equals 3.0 +- 0.0001
    mr.features(5) equals 2.7 +- 0.0001
  }

  test("Values of features 1") {
    val mr = wm.map(Seq("a", "d", "c"))
    mr.features(0) equals 0.0 +- 0.0001
    mr.features(1) equals 1.5 +- 0.0001
    mr.features(2) equals 4.0 +- 0.0001
    mr.features(3) equals 5.0 +- 0.0001
    mr.features(4) equals 3.0 +- 0.0001
    mr.features(5) equals 2.7 +- 0.0001
  }

}
