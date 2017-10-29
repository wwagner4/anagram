package anagram.solve

import anagram.common.SortedList

sealed trait SolveResult


trait SolverIter extends Iterator[Seq[Ana]]{

  override def hasNext: Boolean

  override def next():Seq[Ana]

}

object SolverIter {

  def instance(anas: Stream[Ana], resultLength: Int): SolverIter = {

    val sl = SortedList.instance(new OrderingAnaRatingDesc)

    var _hasNext = true
    var _askedNever = true
    anas.foreach(ana => sl.add(ana))
    _hasNext = false

    new SolverIter {

      override def hasNext: Boolean = _hasNext || _askedNever

      override def next(): Seq[Ana] = {
        val  re = sl.take(resultLength)
        _askedNever = false
        re
      }
    }
  }

}
