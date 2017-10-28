package anagram.common

case class LinearAdjustParam(a: Double, k: Double)

object LinearAdjust {

  def adjust(adjustments: Map[Int, LinearAdjustParam])(rating: Double, sentLength: Int): Double = {
    val adj = adjustments(sentLength)
    (rating - adj.a) / adj.k
  }

}
