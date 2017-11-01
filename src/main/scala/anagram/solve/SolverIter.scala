package anagram.solve

import anagram.common.SortedList

import scala.concurrent.{ExecutionContextExecutor, Future}

sealed trait SolveResult


trait SolverIter extends Iterator[Seq[Ana]]

object SolverIter {

  def instance(anas: Stream[Ana], resultLength: Int)(implicit executor: ExecutionContextExecutor): SolverIter = {

    val sl = SortedList.instance(new OrderingAnaRatingDesc)

    var deliveredLast = false

    val future = Future {
      anas.foreach(ana => sl.add(ana))
    }

    new SolverIter {

      override def hasNext: Boolean = !future.isCompleted || !deliveredLast

      override def next(): Seq[Ana] = {
        val re = sl.take(resultLength)
        if (future.isCompleted) deliveredLast = true
        re
      }
    }
  }

}
