package anagram.model.grm

import anagram.ml.data.common._
import anagram.model._
import anagram.words.{WordMapper, WordMapperRating, Wordlists}

class CfgModelGrm extends CfgModel {

  private val _dataId = "grm001"
  private val _sentenceLengths = Seq(
    new SentenceLength {
      val length = 2
      override val createDataOutputFactor = 0.00032
      val trainingIterations = 1000
      val trainingBatchSize = 2000
      val trainingLearningRate = 1E-6
      val trainingIterationListenerUpdateCount = 10
      val ratingAdjustOutput = 1.39
    },
    new SentenceLength {
      val length = 3
      override val createDataOutputFactor = 0.003
      val trainingIterations = 200
      val trainingBatchSize = 20000
      val trainingLearningRate = 50E-6
      val trainingIterationListenerUpdateCount = 4
      val ratingAdjustOutput = 1.33
    },
    new SentenceLength {
      val length = 4
      override val createDataOutputFactor = 0.02
      val trainingIterations = 100
      val trainingBatchSize = 100000
      val trainingLearningRate = 50E-6
      val trainingIterationListenerUpdateCount = 10
      val ratingAdjustOutput = 0.89
    },
    new SentenceLength {
      val length = 5
      override val createDataOutputFactor = 0.1
      val trainingIterations = 100
      val trainingBatchSize = 200000
      val trainingLearningRate = 10E-6
      val trainingIterationListenerUpdateCount = 5
      val ratingAdjustOutput = 0
    },
  )

  private lazy val _bookCollection = BookCollections.collectionEn2

  private lazy val _wl = Wordlists.grammar.wordList()

  private lazy val _mapper = new WordMapperFactoryGrammar(_wl).create

  private lazy val _grp = new GrouperGrm(_wl)

  private val screator = new SentenceCreatorSliding(_mapper)

  private val _lfs = _sentenceLengths.map(sl => (sl.length, sl.createDataOutputFactor)).toMap

  private val srater = SentenceLabelerCounting(_lfs, _mapper, _grp)

  override lazy val cfgCreateData: CfgCreateDataFactory = {


    lazy val cfg = new CfgCreateData {

      override def id: String = _dataId

      override def sentenceLengths: Iterable[SentenceLength] = _sentenceLengths

      override def mapper: WordMapper = _mapper

      override def sentenceCreator: SentenceCreator = screator

      override def sentenceLabeler: SentenceLabeler = srater

      override def bookCollection: BookCollection = _bookCollection

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

      override def mapper: WordMapperRating = _mapper

      override def adjustOutput: Boolean = true

    }
    new CfgRaterAiFactory {

      override def description: String = s"Grammar ${_dataId}"

      override def shortDescription: String = s"GRM_${_dataId}"

      override def cfgRaterAi: () => CfgRaterAi = () => cfg

    }
  }


}
