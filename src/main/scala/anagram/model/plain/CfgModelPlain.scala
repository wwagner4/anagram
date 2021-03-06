package anagram.model.plain

import anagram.ml.data.common._
import anagram.model._
import anagram.words.{WordMapper, WordMapperRating, Wordlists}

class CfgModelPlain extends CfgModel[Seq[String]] {

  private val _dataId = "plain001"

  private val _batchSize = 10000
  private val _learningRate = 1.0E-5
  private val _iterationListenerUpdateCount = 10

  private val _sentenceLengths = Seq(
    new  SentenceLength {
      val length = 3
      val trainingIterations = 2
      val ratingAdjustOutput = 0.39
      val trainingBatchSize: Int = _batchSize
      val trainingLearningRate: Double = _learningRate
      val trainingIterationListenerUpdateCount: Int = _iterationListenerUpdateCount
    },
    new  SentenceLength {
      val length = 4
      val trainingIterations = 2
      val ratingAdjustOutput = 0.0
      val trainingBatchSize: Int = _batchSize
      val trainingLearningRate: Double = _learningRate
      val trainingIterationListenerUpdateCount: Int = _iterationListenerUpdateCount
    },
    new  SentenceLength {
      val length = 5
      val trainingIterations = 3
      val ratingAdjustOutput = 0.17
      val trainingBatchSize: Int = _batchSize
      val trainingLearningRate: Double = _learningRate
      val trainingIterationListenerUpdateCount: Int = _iterationListenerUpdateCount
    },
  )
  private lazy val _bookCollection = BookCollections.collectionEn2

  private lazy val _wl = Wordlists.plainRatedLarge.wordList()

  private lazy val _mapper = new WordMapperFactoryPlain(_wl).create
  val splitter = new BookSplitterTxt()
  val screator = new SentenceCreatorSliding
  lazy val srater = new SentenceLabelerStraight(_mapper)

  override lazy val cfgCreateData: CfgCreateDataFactory[Seq[String]] = {
    lazy val cfg = new CfgCreateData[Seq[String]] {

      override def id: String = _dataId

      override def sentenceLengths: Iterable[SentenceLength] = _sentenceLengths

      override def mapper: WordMapper[Seq[String]] = _mapper

      override def sentenceCreator: SentenceCreator = screator

      override def sentenceLabeler: SentenceLabeler = srater

      override def bookCollection: BookCollection = _bookCollection

    }
    new CfgCreateDataFactory[Seq[String]] {
      override def cfgCreateData: () => CfgCreateData[Seq[String]] = () => cfg
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

      override def mapper: WordMapperRating[Seq[String]] = _mapper

      override def adjustOutput: Boolean = true

    }
    new CfgRaterAiFactory {
      override def description: String = s"Plain ${_dataId}"

      override def shortDescription: String = s"PLAIN_${_dataId}"

      override def cfgRaterAi: () => CfgRaterAi = () => cfg
    }
  }


}
