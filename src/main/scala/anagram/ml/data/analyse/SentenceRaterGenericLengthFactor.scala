package anagram.ml.data.analyse

import anagram.common.IoUtil

case class GenRating(sentLen: Int, rating: Double)

/**
  * The NNs for different Length produce output values
  * in different ranges. This program calculates a factor for
  * each sentence length, in order to make the output values
  * for sentences with different lengths comparable.
  */
object SentenceRaterGenericLengthFactor extends App {

  val ref = 10.0
  val fileNames = Seq(
    "en04_clint_eastwood.txt",
    "en04_william_shakespeare.txt",
  )


  for (fn <- fileNames) {
    val maxRatings: Seq[(Int, Double, Double)] = read(fn)
      .map(adjust)
      .groupBy(x => x.sentLen)
      .toList
      .map { case (k, v) => (k, v.map(_.rating).max) }
      .sortBy { case (k, _) => k }
      .map { case (k, v) => (k, v, ref / v ) }

    println(s"Max ratings for $fn")
    for ((len, maxRating, fact) <- maxRatings) {
      println(f"$len%10d - $maxRating%6.4f - $fact%6.4f")
    }
  }

  def read(fileName: String): Iterable[GenRating] = {
    IoUtil.loadTxtFromWorkdir(fileName, (iter) => {
      iter.toIterable.map(s => mkGenRating(s))
    })
  }

  def adjust(genRating: GenRating): GenRating = {
    val newRating = genRating.rating / lengthFactor(genRating.sentLen)
    genRating.copy(rating = newRating)
  }

  def mkGenRating(s: String): GenRating = {
    val idx1 = s.indexOf('-', 0)
    require(idx1 >= 0)
    val idx2 = s.indexOf('-', idx1 + 1)
    require(idx2 >= 0)
    val idx3 = s.indexOf('\'', 0)
    require(idx3 >= 0)
    val idx4 = s.indexOf('\'', idx3 + 1)
    require(idx4 >= 0)
    val ratingStr = s.substring(idx1 + 2, idx2 - 1)
    val sent = s.substring(idx3, idx4)

    //println(s"'$s' + ($idx1 $idx2) ($idx3 $idx4) :: '$ratingStr' :: '$sent'")

    if (sent.isEmpty || ratingStr.isEmpty) GenRating(0, 0.0)
    else GenRating(sent.split("\\s").length, ratingStr.toDouble)
  }

  def lengthFactor(len: Int): Double = {
    if (len <= 1) 2.0
    else if (len <= 2) 1.7
    else if (len <= 3) 1.6
    else if (len <= 4) 1.5
    else if (len <= 5) 1.4
    else if (len <= 6) 1.3
    else if (len <= 7) 1.2
    else if (len <= 8) 1.1
    else 1.0
  }


}
