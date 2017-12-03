package anagram.model.create

import anagram.ml.DataCollector
import anagram.ml.data.common.{BookCollection, CreateLearningData, SentenceCreator, SentenceRater}
import anagram.ml.train.Training
import anagram.model._
import anagram.words.WordMapper

class AbstractCreator {

  protected def all(dataCollector: DataCollector, training: Boolean = true): Unit = {
    Configurations.all.foreach((toCfg: CfgModel) => one(toCfg, dataCollector, training))
  }

  protected def one(toCfg: CfgModel, dataCollector: DataCollector, training: Boolean = true): Unit = {
    CreateLearningData.createData(toCfg.cfgCreateData.cfgCreateData())
    if (training) {
      Training.train(toCfg.cfgTraining.cfgTraining(), dataCollector)
    }
  }

  def unmapped(base: CfgCreateData): Unit = {

    val adapted: CfgCreateDataImpl = CfgCreateDataImpl(
      base.id,
      base.mapper,
      base.sentenceCreator,
      base.sentenceRater,
      base.bookCollection,
      base.sentenceLengths,
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
                              sentenceLengths: Iterable[SentenceLength],
                              mapWordsToNumbers: Boolean,
                            ) extends CfgCreateData
