package anagram.ml.train

import org.deeplearning4j.nn.api.Model
import org.deeplearning4j.optimize.api.IterationListener
import org.slf4j.LoggerFactory

class IterationListenerScore(val printIterations: Int) extends IterationListener {

  private val log = LoggerFactory.getLogger("IterationListenerScore")

  private var _invoked = false
  private var _iterCount = 0
  private var _scores = List.empty[(Int, Double)]

  override def invoke(): Unit = _invoked = true

  override def iterationDone(model: Model, i: Int): Unit = {
    invoked()
    if (_iterCount % printIterations == 0) {
      val s = model.score()
      _scores = (_iterCount, s) :: _scores
      log.info(f"finished ${_iterCount} iterations. score: $s%.5f")
    }
    _iterCount += 1
  }

  override def invoked(): Boolean = _invoked

  def scores: Iterable[(Int, Double)] = _scores

}
