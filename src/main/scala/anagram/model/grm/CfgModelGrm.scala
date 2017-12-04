package anagram.model.grm

import anagram.ml.data.common._
import anagram.model._
import anagram.words.{WordMapper, WordMapperPrediction}

class CfgModelGrm extends CfgModel {

  private val _dataId = "grm001"
  private val _sentenceLengths = Seq(
    SentenceLength_2(
      trainingIterations = 2000,
      trainingBatchSize = 2000,
      trainingLearningRate = 1E-6,
      trainingIterationListenerUpdateCount = 100,
      ratingAdjustOutput = 0,
    ),
//    SentenceLength_3(1, 0),
//    SentenceLength_4(1, 0),
//    SentenceLength_5(1, 0),
  )
  private lazy val _bookCollection = BookCollections.collectionEn2

  private lazy val _mapper = WordMapperFactoryGrammar.create

  private val screator = new SentenceCreatorSliding()
  private val srater = new SentenceRaterCounting

  override lazy val cfgCreateData: CfgCreateDataFactory = {


    lazy val cfg = new CfgCreateData {
      override def id: String = _dataId

      override def sentenceLengths: Iterable[SentenceLength] = _sentenceLengths

      override def mapper: WordMapper = _mapper

      override def sentenceCreator: SentenceCreator = screator

      override def sentenceRater: SentenceRater = srater

      override def bookCollection: BookCollection = _bookCollection

      override def mapWordsToNumbers: Boolean = true
    }

    new CfgCreateDataFactory {

      override def cfgCreateData: () => CfgCreateData = () => cfg
    }
  }

  override lazy val cfgTraining: CfgTrainingFactory = {

    lazy val cfg = new CfgTraining {

      override def sentenceLengths: Iterable[SentenceLength] = _sentenceLengths

      override def id: String = _dataId

    }

    new CfgTrainingFactory {

      override def cfgTraining: () => CfgTraining = () => cfg
    }
  }

  override lazy val cfgRaterAi: CfgRaterAiFactory = {
    lazy val cfg = new CfgRaterAi {

      override def sentenceLengths: Iterable[SentenceLength] = _sentenceLengths

      override def id: String = _dataId

      override def mapper: WordMapperPrediction = _mapper

      override def adjustOutput: Boolean = true

    }
    new CfgRaterAiFactory {

      override def description: String = s"Grammar ${_dataId}"

      override def shortDescription: String = s"GRM_${_dataId}"

      override def cfgRaterAi: () => CfgRaterAi = () => cfg

    }
  }


}
