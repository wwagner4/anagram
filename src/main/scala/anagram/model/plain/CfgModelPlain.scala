package anagram.model.plain

import anagram.ml.data.common._
import anagram.model._
import anagram.words.{WordMapper, WordMapperPrediction}

class CfgModelPlain extends CfgModel {

  private val _dataId = "plain001"
  private val _sentenceLengths = Seq(
    SentenceLength_2(2),
    SentenceLength_3(2),
    SentenceLength_4(2),
    SentenceLength_5(3),
  )
  private lazy val _bookCollection = BookCollections.collectionEn2

  private def _adjustOutput(len: Int, rating: Double): Double = {
    len match {
      case 1 => rating + 20
      case 2 => rating + 1.4105
      case 3 => rating + 0.5655
      case 4 => rating + 0.0000
      case 5 => rating + 0.4568
      case _ => rating - 20
    }  }

  private lazy val _mapper = WordMapperFactoryPlain.create
  val splitter = new BookSplitterTxt()
  val screator = new SentenceCreatorSliding()
  lazy val srater = new SentenceRaterStraight(_mapper)

  override lazy val cfgCreateData: CfgCreateDataFactory = {    lazy val cfg = new CfgCreateData {

      override def id: String = _dataId

      override def sentenceLength: Iterable[SentenceLength] = _sentenceLengths

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

      override def adjustOutputFunc: (Int, Double) => Double = _adjustOutput

      override def adjustOutput: Boolean = true

    }
    new CfgRaterAiFactory {override def description: String = s"Plain ${_dataId}"

      override def shortDescription: String = s"PLAIN_${_dataId}"

      override def cfgRaterAi: () => CfgRaterAi = () => cfg
    }
  }




}
