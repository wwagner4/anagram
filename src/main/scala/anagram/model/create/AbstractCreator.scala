package anagram.model.create

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import anagram.ml.DataCollector
import anagram.ml.data.common.CreateLearningData
import anagram.ml.train.Training
import anagram.model._

class AbstractCreator {

  protected def all(dataCollector: DataCollector, training: Boolean = true): Unit = {
    Configurations.all.foreach(toCfg => one(toCfg, dataCollector, training))
  }

  protected def one(toCfg: CfgModel[_], dataCollector: DataCollector, training: Boolean = true): Unit = {
    CreateLearningData.createData(toCfg.cfgCreateData.cfgCreateData())
    if (training) {
      Training.train(toCfg.cfgTraining.cfgTraining(), dataCollector)
    }
  }

  protected def timestamp: String = {
    val dt = LocalDateTime.now()
    val f = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    f.format(dt)
  }



}

