package anagram.impl

import anagram.AnagramMorph

class AnagramMorphLinear extends AnagramMorph {

  def morph(from: String, to: String, lines: Int): List[String] = {
    ???
  }

  def assign(a: String, b: String): Seq[Int] = {

    def assign(al: List[Char], bl: List[Char], indexes: List[Int]): List[Int] = {

      def firstUnusedIndex(char: Char, txt: List[Char], currentIndex: Int, usedIndexes: List[Int]): Int = {
        txt match {
          case Nil => throw new IllegalStateException(s"found no index for $char")
          case head :: tail =>
            if (char == head && !usedIndexes.contains(currentIndex)) currentIndex
            else firstUnusedIndex(char, tail, currentIndex + 1, usedIndexes)
        }
      }

      al match {
        case Nil => indexes.reverse
        case head :: tail =>
          assign(tail, bl, firstUnusedIndex(head, bl, 0, indexes) :: indexes)
      }
    }
    if (a.length != b.length) throw new IllegalStateException("a and b must have same length")
    assign(a.toList, b.toList, List.empty[Int])
  }

  def morphIndex(target: Seq[Int], numLines: Int): Seq[Seq[Int]] = {

    def flin(a: Double, k: Double)(x: Double): Double = a + k * x

    val fList: Seq[(Double) => Double] = for(i <- target.indices) yield {
      val a = i.toDouble
      val k = (target.indexOf(i) - i).toDouble / (numLines - 1)
      flin(a, k)(_)
    }
    for (x <- 0 until numLines) yield {
      fList
        .map(_(x))
        .zipWithIndex
        .sortBy(_._1)
        .map(_._2)
    }
  }


}
