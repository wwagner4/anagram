package anagram.model

import anagram.ml.data.common.CreateLearningData
import anagram.ml.train.Training

object Creator extends App {

  for (toCfg <- Configurations.all) {
    CreateLearningData().createData(toCfg().cfgCreateData)
    Training.train(toCfg().cfgTraining)
  }


}
