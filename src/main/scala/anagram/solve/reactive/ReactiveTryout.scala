package anagram.solve.reactive

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging

import scala.util.Random


object ReactiveTryout extends App {

  case class Result(texts: Seq[String])

  println("HI REACTIVE")
  val system = ActorSystem("anagram")

  val testActorRef: ActorRef = system.actorOf(Props[MasterActor], "test")

  testActorRef ! (1 to 100).map(_.toString)

  class MasterActor extends Actor {
    val log = Logging(context.system, this)

    val _ctx = context

    def receive = {
      case txts: Iterable[String] => send(txts)
      case Result(rs) => log.info("Received" + rs)
      case _ => log.info("received unknown message")
    }

    def send(txts: Iterable[String]): Unit = {
      for (str <- txts) {
        val w = context.actorOf(Props[WorkerActor])
        w ! str
      }
    }
  }

  class WorkerActor extends Actor {
    val log = Logging(context.system, this)
    val delay = 10 + Random.nextInt(20)

    def receive = {
      case txt: String => sender ! createResult(txt)
      case _ => log.info("received unknown message")
    }

    def createResult(txt: String): Unit = {
      val rs = (1 to 10).map{i =>
        Thread.sleep(delay)
        s"$txt-$i"}
      sender ! Result(rs)
    }

  }

  Thread.sleep(15000)

  system.terminate()

}
