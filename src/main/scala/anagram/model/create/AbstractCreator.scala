package anagram.model.create

import anagram.ml.DataCollector
import anagram.ml.data.common.{BookCollection, CreateLearningData, SentenceCreator, SentenceRater}
import anagram.ml.train.Training
import anagram.model.{CfgCreateData, CfgModel, Configurations}
import anagram.words.WordMapper

class AbstractCreator {

  protected def all(training: Boolean, dataCollector: DataCollector): Unit = {
    for (toCfg <- Configurations.all) {
      one(toCfg, training, dataCollector)
    }
  }

  protected def one(toCfg: CfgModel, training: Boolean, dataCollector: DataCollector): Unit = {
    CreateLearningData.createData(toCfg.cfgCreateData.cfgCreateData())
    if (training) {
      Training.train(toCfg.cfgTraining.cfgTraining(), dataCollector)
    }
  }

  def unmapped(base: CfgCreateData): Unit = {
    val adapted = CfgCreateDataImpl(
      base.id,
      base.mapper,
      base.sentenceCreator,
      base.sentenceRater,
      base.bookCollection,
      Seq(3, 4),
      base.adjustRating,
      mapWordsToNumbers = false,
    )

    CreateLearningData.createData(adapted)
  }


}

case class CfgCreateDataImpl(
                              id: String,
                              mapper: WordMapper,
                              sentenceCreator: SentenceCreator,
                              sentenceRater: SentenceRater,
                              bookCollection: BookCollection,
                              sentenceLength: Iterable[Int],
                              adjustRating: (Double, Int) => Double,
                              mapWordsToNumbers: Boolean,
                            ) extends CfgCreateData
