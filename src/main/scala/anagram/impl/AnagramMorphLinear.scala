package anagram.impl

import anagram.AnagramMorph

class AnagramMorphLinear extends AnagramMorph {

  sealed trait NumBlanksSuperior

  case object NumBlanksSuperior_NONE extends NumBlanksSuperior

  case class NumBlanksSuperior_FROM(fromAdjusted: String, idxToBeRemoved: Seq[Int]) extends NumBlanksSuperior

  case class NumBlanksSuperior_TO(toAdjusted: String, idxToBeRemoved: Seq[Int]) extends NumBlanksSuperior

  def morph(from: String, to: String, lines: Int): Seq[String] = {
    require(!hasDoubleBlanks(from), "No double blanks allowed")
    require(!hasDoubleBlanks(to), "No double blanks allowed")
    val blanksFrom: Int = countBlanks(from)
    val blanksTo: Int = countBlanks(to)
    val num: NumBlanksSuperior =
      if (blanksFrom > blanksTo) {
        val (adj, idx) = removeBlanks(from, blanksFrom - blanksTo)
        NumBlanksSuperior_FROM(adj, idx)
      } else if (blanksTo > blanksFrom) {
        val (adj, idx) = removeBlanks(to, blanksTo - blanksFrom)
        NumBlanksSuperior_TO(adj, idx)
      }
      else {
        NumBlanksSuperior_NONE
      }
    val re = num match {
      case NumBlanksSuperior_NONE => morph1(from, to, lines).reverse
      case NumBlanksSuperior_FROM(adj, idx) =>
        for ((morphed, line) <- morph1(adj, to, lines).reverse.zipWithIndex) yield {
          if (line >= lines - 2) morphed
          else addBlanksIfPossible(morphed, idx)
        }
      case NumBlanksSuperior_TO(adj, idx) =>
        for ((morphed, line) <- morph1(from, adj, lines).reverse.zipWithIndex) yield {
          if (line < 2) morphed
          else addBlanksIfPossible(morphed, idx)
        }
    }
    re.map(moveBlanksInwards)
  }

  val placeholder: Char = '\u0C7F'

  def replaceWithPlaceholder(txt: List[Char], idx: Int, ph: Char): List[Char] =
    for ((c, i) <- txt.zipWithIndex) yield if (i == idx) ph else c

  def findToIndexes(from: String, to: String): List[Int] = {

    def find(f: List[Char], t: List[Char]): List[Int] = {
      t match {
        case Nil => Nil
        case x :: rest =>
          val i = f.indexOf(x)
          val r = replaceWithPlaceholder(f, i, placeholder)
          i :: find(r, rest)
      }
    }

    find(from.toList, to.toList)
  }

  def createCharMap(txt: String): Map[Int, Char] = {
    val tu = txt.zipWithIndex.map(tu => (tu._2, tu._1))
    tu.toMap
  }

  def morph1(from: String, to: String, lines: Int): Seq[String] = {
    val m = createCharMap(from)
    val toIndexes: List[Int] = findToIndexes(from, to)
    val morphIndexes: Seq[Seq[Int]] = morphIndex(toIndexes, lines)
    morphIndexes.map(idx => idx.map(i => m(i)).mkString).reverse
  }

  def addBlanksIfPossible(txt: String, idx: Seq[Int]): String = {
    idx.foldLeft(txt)((txt, i) => if (canAddBlank(txt, i)) addBlanks(txt, Seq(i)) else txt)
  }

  def hasDoubleBlanks(txt: String): Boolean = {

    def hasDoubleBlanks1(chars: List[Char]): Boolean = {
      chars match {
        case Nil => false
        case _ :: Nil => false
        case c1 :: c2 :: _ if c1 == ' ' && c2 == ' ' => true
        case _ :: c2 :: rest => hasDoubleBlanks1(c2 :: rest)
      }
    }

    hasDoubleBlanks1(txt.toList)

  }

  def countBlanks(txt: String): Int = txt.toList.count(c => c == ' ')

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

    val fList: Seq[(Double) => Double] = for (i <- target.indices) yield {
      val a = i.toDouble
      val k = (target.indexOf(i) - i).toDouble / (numLines - 1)
      flin(a, k)(_)
    }
    for (x <- 0 until numLines) yield {
      fList
        .map(_ (x))
        .zipWithIndex
        .sortBy(_._1)
        .map(_._2)
    }
  }

  def removeBlanks(txt: String, numToBeRemoved: Int): (String, Seq[Int]) = {
    val is: Seq[Int] = txt.toList
      .zipWithIndex
      .filter(_._1 == ' ')
      .map(_._2)
    val middle: Int = txt.length / 2
    val idxToBeRemoved = is
      .map(i => (i, math.abs(i - middle)))
      .sortBy(_._2)
      .take(numToBeRemoved)
      .map(_._1)
    val txtOut = txt.zipWithIndex
      .filter(t => !idxToBeRemoved.contains(t._2))
      .map(_._1)
      .mkString("")
    (txtOut, idxToBeRemoved.sorted)
  }

  def canAddBlank(txt: String, index: Int): Boolean = {
    require(index >= 0 && index <= txt.length)
    if (index == 0) txt(index) != ' '
    else if (index == txt.length) txt(index - 1) != ' '
    else txt(index - 1) != ' ' && txt(index) != ' '
  }


  def addBlanks(txt: String, indexes: Seq[Int]): String = {
    def addBlanks(txt: List[Char], index: Int, indexes: Seq[Int]): List[Char] = {
      txt match {
        case Nil => Nil
        case head :: tail =>
          if (indexes.contains(index)) ' ' :: addBlanks(head :: tail, index + 1, indexes)
          else head :: addBlanks(tail, index + 1, indexes)
      }
    }

    addBlanks(txt.toList, 0, indexes).mkString
  }

  def moveBlanksInwards(txt: String): String = {
    def moveBlanksInwardsFirst(txt: String): String = {
      if (txt.isEmpty) txt
      else if (txt.length == 1) txt
      else if (txt.head == ' ') txt.toList match {
        case a :: b :: rest => (b :: a :: rest).mkString
        case _ => throw new IllegalArgumentException("should never come here")
      }
      else txt
    }

    def moveBlanksInwardsLast(txt: String): String = {
      if (txt.isEmpty) txt
      else if (txt.length == 1) txt
      else if (txt.last == ' ') txt.toList.reverse match {
        case a :: b :: rest => (b :: a :: rest).reverse.mkString
        case _ => throw new IllegalArgumentException("should never come here")
      }
      else txt
    }

    moveBlanksInwardsLast(moveBlanksInwardsFirst(txt))
  }


}
