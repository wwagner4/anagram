package anagram.model.plainrandom

import anagram.ml.data.common._
import anagram.model._
import anagram.model.plain.WordMapperFactoryPlain
import anagram.words.{WordMapper, WordMapperPrediction}

class CfgModelPlainRandom extends CfgModel {

  private val _dataId = "plainRand001"
  private val _sentenceLengths = Seq(
    SentenceLength_2(
      trainingIterations = 2,
      ratingAdjustOutput = 0.72,
    ),
    SentenceLength_3(
      trainingIterations = 2,
      ratingAdjustOutput = 0.52,
    ),
    SentenceLength_4(
      trainingIterations = 2,
      ratingAdjustOutput = 0.0,
    ),
    SentenceLength_5(
      trainingIterations =  2,
      ratingAdjustOutput = 0.11,
    ),
  )
  private lazy val _bookCollection = BookCollections.collectionEn2

  private lazy val _mapper = WordMapperFactoryPlain.create
  val splitter = new BookSplitterTxt()
  val screator = new SentenceCreatorSliding()
  lazy val srater = new SentenceRaterStraightWithRandom(_mapper)

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

      override def batchSize: Int = 10000

      override def learningRate: Double = 1.0E-5

      override def iterationListenerUpdateCount: Int = 10

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
      override def description: String = s"Plain random ${_dataId}"

      override def shortDescription: String = s"PLAINR_${_dataId}"

      override def cfgRaterAi: () => CfgRaterAi = () => cfg
    }
  }


}
