package anagram.ml.train

import anagram.ml.DataCollectorNull
import anagram.model._

object TrainingTryout extends App {

  val cfgm: CfgModel[_] = Configurations.plainRated
  val cfg: CfgTraining = cfgm.cfgTraining.cfgTraining()

  val dc = new DataCollectorNull

  Training.train(cfg, dc)
}
