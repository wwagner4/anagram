

package anagram.solve.concurrent

import java.util.concurrent._
import java.util.concurrent.atomic.AtomicInteger

import scala.annotation.tailrec
import scala.concurrent.{BlockContext, CanAwait}

/**
  * Copy of the scala global execution context.
  * Had to extract that code in order to create an Execution context from an existing
  * ExecutorService.
  */
object AnaExecutionContextImpl {

  def createDefaultExecutorService(reporter: Throwable => Unit): ExecutorService = {
    def getInt(name: String, default: String) = (try System.getProperty(name, default) catch {
      case _: SecurityException => default
    }) match {
      case s if s.charAt(0) == 'x' => (Runtime.getRuntime.availableProcessors * s.substring(1).toDouble).ceil.toInt
      case other => other.toInt
    }

    def range(floor: Int, desired: Int, ceiling: Int) = scala.math.min(scala.math.max(floor, desired), ceiling)
    val numThreads = getInt("scala.concurrent.context.numThreads", "x1")
    // The hard limit on the number of active threads that the thread factory will produce
    // scala/bug#8955 Deadlocks can happen if maxNoOfThreads is too low, although we're currently not sure
    //         about what the exact threshold is. numThreads + 256 is conservatively high.
    val maxNoOfThreads = getInt("scala.concurrent.context.maxThreads", "x1")

    val desiredParallelism = range(
      getInt("scala.concurrent.context.minThreads", "1"),
      numThreads,
      maxNoOfThreads)

    // The thread factory must provide additional threads to support managed blocking.
    val maxExtraThreads = getInt("scala.concurrent.context.maxExtraThreads", "256")

    val uncaughtExceptionHandler: Thread.UncaughtExceptionHandler = (_: Thread, cause: Throwable) => reporter(cause)

    val threadFactory = new AnaExecutionContextImpl.DefaultThreadFactory(daemonic = true,
      maxThreads = maxNoOfThreads + maxExtraThreads,
      prefix = "scala-execution-context-global",
      uncaught = uncaughtExceptionHandler)

    new ForkJoinPool(desiredParallelism, threadFactory, uncaughtExceptionHandler, true) {
      override def execute(runnable: Runnable): Unit = {
        val fjt: ForkJoinTask[_] = runnable match {
          case t: ForkJoinTask[_] => t
          case r                  => new AnaExecutionContextImpl.AdaptedForkJoinTask(r)
        }
        Thread.currentThread match {
          case fjw: ForkJoinWorkerThread if fjw.getPool eq this => fjt.fork()
          case _                                                => super.execute(fjt)
        }
      }
    }
  }

  // Implement BlockContext on FJP threads
  final class DefaultThreadFactory(
                                    daemonic: Boolean,
                                    maxThreads: Int,
                                    prefix: String,
                                    uncaught: Thread.UncaughtExceptionHandler) extends ThreadFactory with ForkJoinPool.ForkJoinWorkerThreadFactory {

    require(prefix ne null, "DefaultThreadFactory.prefix must be non null")
    require(maxThreads > 0, "DefaultThreadFactory.maxThreads must be greater than 0")

    private final val currentNumberOfThreads = new AtomicInteger(0)

    @tailrec private def reserveThread(): Boolean = currentNumberOfThreads.get() match {
      case `maxThreads` | Int.`MaxValue` => false
      case other => currentNumberOfThreads.compareAndSet(other, other + 1) || reserveThread()
    }

    @tailrec private def deregisterThread(): Boolean = currentNumberOfThreads.get() match {
      case 0 => false
      case other => currentNumberOfThreads.compareAndSet(other, other - 1) || deregisterThread()
    }

    def wire[T <: Thread](thread: T): T = {
      thread.setDaemon(daemonic)
      thread.setUncaughtExceptionHandler(uncaught)
      thread.setName(prefix + "-" + thread.getId)
      thread
    }

    // As per ThreadFactory contract newThread should return `null` if cannot create new thread.
    def newThread(runnable: Runnable): Thread =
      if (reserveThread())
        wire(new Thread(() => try runnable.run() finally deregisterThread())) else null

    def newThread(fjp: ForkJoinPool): ForkJoinWorkerThread =
      if (reserveThread()) {
        wire(new ForkJoinWorkerThread(fjp) with BlockContext {
          // We have to decrement the current thread count when the thread exits
          final override def onTermination(exception: Throwable): Unit = deregisterThread()
          final override def blockOn[T](thunk: =>T)(implicit permission: CanAwait): T = {
            var result: T = null.asInstanceOf[T]
            ForkJoinPool.managedBlock(new ForkJoinPool.ManagedBlocker {
              @volatile var isdone = false
              override def block(): Boolean = {
                result = try {
                  // When we block, switch out the BlockContext temporarily so that nested blocking does not created N new Threads
                  BlockContext.withBlockContext(BlockContext.defaultBlockContext) { thunk }
                } finally {
                  isdone = true
                }

                true
              }
              override def isReleasable: Boolean = isdone
            })
            result
          }
        })
      } else null
  }

  private final class AdaptedForkJoinTask(runnable: Runnable) extends ForkJoinTask[Unit] {
    override def setRawResult(u: Unit): Unit = ()
    override def getRawResult: Unit = ()
    override def exec(): Boolean = try { runnable.run(); true } catch {
      case anything: Throwable =>
        val t = Thread.currentThread
        t.getUncaughtExceptionHandler match {
          case null =>
          case some => some.uncaughtException(t, anything)
        }
        throw anything
    }
  }
}


