package anagram.model

import anagram.ml.data.common.CreateLearningData
import anagram.ml.train.Training

object Creator extends App {


  one(Configurations.grammarReduced)

  private def all(): Unit = {
    for (toCfg <- Configurations.all) {
      one(toCfg())
    }
  }

  private def one(toCfg: CfgModel) = {
    CreateLearningData().createData(toCfg.cfgCreateData)
    Training.train(toCfg.cfgTraining)
  }
}
