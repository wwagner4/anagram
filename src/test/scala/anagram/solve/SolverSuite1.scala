package anagram.solve

import org.scalatest.{FunSuite, MustMatchers}

class SolverSuite1 extends FunSuite with MustMatchers {

  case class RE(rating: Double, words: Seq[String])

  val expectedPlain1 = Seq(
    ("wolfgang", "PLAIN_plain001", Seq(
      RE(32.9957, List("flag", "gown")),
      RE(33.4897, List("gown", "flag")))),
    ("ditschi", "PLAIN_plain001", Seq(
      RE(33.589, List("is", "ditch")),
      RE(33.6183, List("hit", "disc")))),
    ("ingrid bernd", "PLAIN_plain001", Seq(
      RE(32.6135, List("in", "in", "beg", "dr", "dr")),
      RE(32.6135, List("in", "in", "beg", "rr", "dd")))),
  )

  val expectedPlainRand1 = Seq(
    ("wolfgang", "PLAINR_plainRand001", Seq(
      RE(22.10758, List("flag", "gown")),
      RE(22.50175, List("gown", "flag")))),
    ("ditschi", "PLAINR_plainRand001", Seq(
      RE(22.657, List("is", "ditch")),
      RE(22.65767, List("hit", "disc")))),
    ("ingrid bernd", "PLAINR_plainRand001", Seq(
      RE(21.4158, List("in", "in", "beg", "dr", "dr")),
      RE(21.4158, List("in", "in", "beg", "rr", "dd")))),
  )

  val expectedGrm1 = Seq(
    ("wolfgang", "GRM_grm001", Seq(
      RE(0.56, List("flag", "gown")),
      RE(0.52, List("gown", "flag")))),
    ("ditschi", "GRM_grm001", Seq(
      RE(0.50, List("is", "ditch")),
      RE(0.49, List("hit", "disc")))),
    ("ingrid bernd", "GRM_grm001", Seq(
      RE(0.29, List("in", "in", "beg", "dr", "dr")),
      RE(0.29, List("in", "in", "beg", "rr", "dd")))),
  )

  val expectedGrmRed1 = Seq(
    ("wolfgang", "GRMRED_grmRed001", Seq(
      RE(1.82, List("flag", "gown")),
      RE(1.80, List("gown", "flag")))),
    ("ditschi", "GRMRED_grmRed001", Seq(
      RE(1.82, List("is", "ditch")),
      RE(1.80, List("hit", "disc")))),
    ("ingrid bernd", "GRMRED_grmRed001", Seq(
      RE(1.71, List("in", "in", "beg", "dr", "dr")),
      RE(1.71, List("in", "in", "beg", "rr", "dd")))),
  )


}
