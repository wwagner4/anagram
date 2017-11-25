package anagram

import java.util.concurrent.{Executors, TimeUnit}

import anagram.common.Cancelable

object Scheduler {

  def schedule(delay: Int)(f: () => Unit): Cancelable = {
    val ses = Executors.newSingleThreadScheduledExecutor()
    val runnable = new Runnable {
      override def run(): Unit = f()
    }
    ses.scheduleAtFixedRate(runnable, 0, delay, TimeUnit.MILLISECONDS)
    () => ses.shutdownNow()
  }

}
