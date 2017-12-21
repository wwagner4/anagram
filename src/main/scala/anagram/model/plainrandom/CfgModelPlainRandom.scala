package anagram.model.plainrandom

import anagram.ml.data.common._
import anagram.model._
import anagram.model.plain.{WordMapperFactoryPlain, WordRandomPlain}
import anagram.words.{WordMapper, WordMapperRating, Wordlists}

class CfgModelPlainRandom extends CfgModel {

  private val _dataId = "plainRand001"

  private val _batchSize = 10000
  private val _learningRate = 1.0E-5
  private val _iterationListenerUpdateCount = 10

  private val _sentenceLengths = Seq(
    new SentenceLength {
      val length = 2
      val trainingBatchSize: Int = _batchSize
      val trainingLearningRate: Double = _learningRate
      val trainingIterationListenerUpdateCount: Int = _iterationListenerUpdateCount
      val trainingIterations = 2
      val ratingAdjustOutput = 0.94
    },
    new SentenceLength {
      val length = 3
      val trainingBatchSize: Int = _batchSize
      val trainingLearningRate: Double = _learningRate
      val trainingIterationListenerUpdateCount: Int = _iterationListenerUpdateCount
      val trainingIterations = 2
      val ratingAdjustOutput = 0.34
    },
    new SentenceLength {
      val length = 4
      val trainingBatchSize: Int = _batchSize
      val trainingLearningRate: Double = _learningRate
      val trainingIterationListenerUpdateCount: Int = _iterationListenerUpdateCount
      val trainingIterations = 2
      val ratingAdjustOutput = 0.0
    },
    new SentenceLength {
      val length = 5
      val trainingIterations = 2
      val ratingAdjustOutput = 0.006
      val trainingBatchSize: Int = _batchSize
      val trainingLearningRate: Double = _learningRate
      val trainingIterationListenerUpdateCount: Int = _iterationListenerUpdateCount
    },
  )
  private lazy val _bookCollection = BookCollections.collectionEn2

  private val _wl = Wordlists.plainRatedLarge.wordList()
  private lazy val _mapper = new WordMapperFactoryPlain(_wl).create
  val splitter = new BookSplitterTxt()
  val screator = new SentenceCreatorSliding(_mapper)
  val ran = new WordRandomPlain(_wl)
  lazy val srater = new SentenceLabelerStraightWithRandom(_mapper, ran)

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
      override def description: String = s"Plain random ${_dataId}"

      override def shortDescription: String = s"PLAINR_${_dataId}"

      override def cfgRaterAi: () => CfgRaterAi = () => cfg
    }
  }

}
