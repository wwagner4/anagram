package anagram.model.create

import anagram.ml.DataCollector
import anagram.ml.data.common.{BookCollection, CreateLearningData, SentenceCreator, SentenceRater}
import anagram.ml.train.Training
import anagram.model.{CfgCreateData, CfgModel, Configurations}
import anagram.words.WordMapper

class AbstractCreator {

  protected def all(dataCollector: DataCollector, training: Boolean = true, adjustLearningDataRating: Boolean = true): Unit = {
    for (toCfg <- Configurations.all) {
      one(toCfg, dataCollector, training, adjustLearningDataRating)
    }
  }

  protected def one(toCfg: CfgModel, dataCollector: DataCollector, training: Boolean = true, adjustLearningDataRating: Boolean = true): Unit = {
    CreateLearningData.createData(toCfg.cfgCreateData.cfgCreateData(), adjustLearningDataRating)
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
