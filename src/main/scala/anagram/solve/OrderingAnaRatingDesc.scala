package anagram.solve

class OrderingAnaRatingDesc extends Ordering[Ana] {

  override def compare(x: Ana, y: Ana): Int = y.rate.compareTo(x.rate)

}
