package anagram.model.grmred

import anagram.ml.data.common._
import anagram.model._
import anagram.words.{WordMapper, WordMapperPrediction}

class CfgModelGrmRed extends CfgModel {

  private val _dataId = "grmRed001"
  private val _sentenceLengths = Seq(
    new SentenceLength {
      val length = 2
      val trainingIterations = 1
      val trainingBatchSize = 0
      val trainingLearningRate = 0.0
      val trainingIterationListenerUpdateCount = 0
      val ratingAdjustOutput = 0
    },
    new SentenceLength {
      val length = 3
      val trainingIterations = 1
      val trainingBatchSize = 0
      val trainingLearningRate = 0.0
      val trainingIterationListenerUpdateCount = 0
      val ratingAdjustOutput = 0
    },
    new SentenceLength {
      val length = 4
      val trainingIterations = 1
      val trainingBatchSize = 0
      val trainingLearningRate = 0.0
      val trainingIterationListenerUpdateCount = 0
      val ratingAdjustOutput = 0
    },
    new SentenceLength {
      val length = 5
      val trainingIterations = 1
      val trainingBatchSize = 0
      val trainingLearningRate = 0.0
      val trainingIterationListenerUpdateCount = 0
      val ratingAdjustOutput = 0
    },
  )

  private lazy val _bookCollection = BookCollections.collectionEn2

  private lazy val _mapper = WordMapperFactoryGrammerReduced.create

  private val screator = new SentenceCreatorSliding()

  private val _lfs = _sentenceLengths.map(sl => (sl.length, sl.createDataOutputFactor)).toMap

  private val srater = SentenceRaterCounting(_lfs)

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

      override def description: String = s"Grammar Reduced ${_dataId}"

      override def shortDescription: String = s"GRMRED_${_dataId}"

      override def cfgRaterAi: () => CfgRaterAi = () => cfg
    }
  }


}
