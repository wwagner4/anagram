package anagram.common

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.Random

object SortedListTryout extends App {

  private implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  private val ran = Random

  private val sl = SortedList.instance[String]

  private val future: Future[Unit] = Future {
    for(i <- 1 to 30) {
      val sorted = sl.take(7).mkString(" ")
      println(s"-- $i - $sorted")
      Thread.sleep(500)
    }
  }

  val flist: Seq[() => Unit] = List.fill(3)(addWords _)

  flist.par.foreach(f => f())

  val re = sl.take(7).mkString(" ")
  println(s"result - $re")

  Await.ready(future, Duration(5, TimeUnit.SECONDS))

  def addWords(): Unit = {
    println("processing 'addWords'")
    for(_ <- 1 to 1000) {
      val rs = randomString
      //    println(s"adding - $rs")
      sl.add(rs)
      Thread.sleep(10)
    }
  }

  def randomString: String = {
    val chars= (1 to 4).map {_ =>
      val i = ran.nextInt(25) + 65
      i.toChar
    }
    chars.mkString
  }

}
