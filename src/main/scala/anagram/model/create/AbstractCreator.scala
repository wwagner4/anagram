package anagram.model.create

import anagram.ml.DataCollector
import anagram.ml.data.common.{BookCollection, CreateLearningData, SentenceCreator, SentenceLabeler}
import anagram.ml.train.Training
import anagram.model._
import anagram.words.WordMapper

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

  def unmapped[T](base: CfgCreateData[T]): Unit = {

    val adapted: CfgCreateDataImpl[T] = CfgCreateDataImpl[T](
      base.id,
      base.mapper,
      base.sentenceCreator,
      base.sentenceLabeler,
      base.bookCollection,
      base.sentenceLengths,
    )

    CreateLearningData.createData(adapted)

  }


}

case class CfgCreateDataImpl[T](
                              id: String,
                              mapper: WordMapper[T],
                              sentenceCreator: SentenceCreator,
                              sentenceLabeler: SentenceLabeler,
                              bookCollection: BookCollection,
                              sentenceLengths: Iterable[SentenceLength],
                            ) extends CfgCreateData[T]
