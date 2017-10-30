package anagram.ml.data.analyze

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
    "anagrams_enGrm11_01_clint_eastwood.txt",
//    "anagrams_enGrm11_01_noah_the_great.txt",
//    "anagrams_enGrm11_01_ditschi_wolfi.txt",
//    "anagrams_enGrm11_01_ditschi.txt",
//    "anagrams_enGrm11_01_ingrid_bernd.txt",
//    "anagrams_enGrm11_01_wolfgang.txt",
  )

    val maxRatings: Seq[(Int, Double)] = fileNames.flatMap(read)
      .groupBy(x => x.sentLen)
      .toList
      .map { case (k, v) => (k, v.map(_.rating).max) }
      .sortBy { case (k, _) => k }

    for ((len, maxRating) <- maxRatings) {
      println(f"$len%10d - $maxRating%6.4f")
    }

  def read(fileName: String): Iterable[GenRating] = {
    IoUtil.loadTxtFromWorkdir(fileName, (iter) => {
      iter.toIterable.map(s => mkGenRating(s))
    })
  }


  // example line
  //   10 - 0.20908 - 'cd hi i fit lows'

  def mkGenRating(s: String): GenRating = {
    val idx1 = s.indexOf('-', 0)
    require(idx1 >= 0)
    val idx2 = s.indexOf("- ", idx1 + 1)
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


}
