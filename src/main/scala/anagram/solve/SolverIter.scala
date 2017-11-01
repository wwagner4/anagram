package anagram.solve

import java.util.concurrent.ExecutorService

import anagram.common.SortedList

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

sealed trait SolveResult


trait SolverIter extends Iterator[Seq[Ana]]{

  def shutdownNow(): Unit

}

object SolverIter {

  def instance(anas: Stream[Ana], resultLength: Int): SolverIter = {

    implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(null)

    lazy val shutdownable = ec.asInstanceOf[ExecutorService]

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

      override def shutdownNow(): Unit = shutdownable.shutdownNow()

    }
  }

}
