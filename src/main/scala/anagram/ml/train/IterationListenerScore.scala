package anagram.ml.train

import anagram.ml.DataCollector
import anagram.model.{CfgTraining, SentenceLength}
import org.deeplearning4j.nn.api.Model
import org.deeplearning4j.optimize.api.IterationListener
import org.slf4j.LoggerFactory

class IterationListenerScore(dataCollector: DataCollector, sentenceLength: SentenceLength, cfg: CfgTraining) extends IterationListener {

  private val log = LoggerFactory.getLogger("IterationListenerScore")

  private var _invoked = false
  private var _iterCount = 0

  override def invoke(): Unit = _invoked = true

  override def iterationDone(model: Model, i: Int): Unit = {
    invoked()
    if (_iterCount % sentenceLength.trainingIterationListenerUpdateCount == 0) {
      val s = model.score()
      dataCollector.collectScore(sentenceLength.length, _iterCount, s)
      log.info(f"finished ${_iterCount} iterations. score: $s%.5f")
    }
    _iterCount += 1
  }

  override def invoked(): Boolean = _invoked

}
