package anagram.ml.train

import anagram.ml.DataCollector
import anagram.model._

object TrainingTryout extends App {

  val cfgm: CfgModel[_] = Configurations.plainRated
  val cfg: CfgTraining = cfgm.cfgTraining.cfgTraining()

  val dc = new DataCollector {
    override def collectScore(sentenceLength: SentenceLength, iterations: Int, score: Double): Unit = () // Nothing to do
  }

  Training.train(cfg, dc)
}
