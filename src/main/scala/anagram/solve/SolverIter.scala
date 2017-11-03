package anagram.solve

import anagram.common.{Cancelable, SortedList}
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContextExecutor, Future}

sealed trait SolveResult


trait SolverIter extends Iterator[Seq[Ana]] with Cancelable {

  def solvedAnagrams: Int

}

object SolverIter {

  private val log = LoggerFactory.getLogger("SolverIter")

  def instance(anas: Iterator[Ana], resultLength: Int)(implicit executor: ExecutionContextExecutor): SolverIter = {

    val sl = SortedList.instance(new OrderingAnaRatingDesc)

    var deliveredLast = false

    new SolverIter {

      private var _canceled = false

      val future = Future {
        while(anas.hasNext && !_canceled) {
          sl.add(anas.next())
        }
      }

      override def hasNext: Boolean = {
        val re = (!future.isCompleted || !deliveredLast) && !_canceled
        log.info(s"[hasNext] $re")
        re
      }

      override def next(): Seq[Ana] = {
        val re = sl.take(resultLength)
        if (future.isCompleted) deliveredLast = true
        re
      }

      override def solvedAnagrams: Int = sl.size

      override def cancel(): Unit = {
        log.info("[cancel]")
        _canceled = true
      }
    }
  }

}
