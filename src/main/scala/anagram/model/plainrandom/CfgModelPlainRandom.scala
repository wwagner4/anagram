package anagram.model.plainrandom

import anagram.common.{LinearAdjust, LinearAdjustParam}
import anagram.ml.data.common._
import anagram.model._
import anagram.model.plain.WordMapperFactoryPlain
import anagram.words.{WordMapper, WordMapperPrediction}

class CfgModelPlainRandom extends CfgModel {

  private val _dataId = "plainRand001"
  private val _sentenceLengths = 2 to 5
  private lazy val _bookCollection = BookCollections.collectionEn2

  private val _adjRating = List(
    (3, LinearAdjustParam(0, 1)),
    (2, LinearAdjustParam(0, 1)),
    (5, LinearAdjustParam(0, 1)),
    (4, LinearAdjustParam(0, 1)),
  ).toMap

  private lazy val _mapper = WordMapperFactoryPlain.create
  val splitter = new BookSplitterTxt()
  val screator = new SentenceCreatorSliding()
  lazy val srater = new SentenceRaterStraightWithRandom(_mapper)

  override lazy val cfgCreateData: CfgCreateDataFactory = {

    lazy val cfg = new CfgCreateData {

      override def id: String = _dataId

      override def adjustRating: (Double, Int) => Double = LinearAdjust.adjust(_adjRating)(_, _)

      override def sentenceLength: Iterable[Int] = _sentenceLengths

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

      override def id: String = _dataId

      override def batchSize: Int = 10000

      override def learningRate: Double = 1.0E-5

      override def iterationListenerUpdateCount: Int = 30

      override def iterations: Int => Int = {
        case 2 => 180
        case 3 => 150
        case 4 => 120
        case 5 => 120
      }

    }
    new CfgTrainingFactory {
      override def cfgTraining: () => CfgTraining = () => cfg
    }
  }

  private def _adjustOutput(len: Int, rating: Double): Double = {
    len match {
      case 1 => rating + 20
      case 2 => rating + 0.3046
      case 3 => rating + 0.1024
      case 4 => rating + 0.0756
      case 5 => rating + 0.0000
      case _ => rating - 20
    }
  }

  override lazy val cfgRaterAi: CfgRaterAiFactory = {
    lazy val cfg = new CfgRaterAi {

      override def id: String = _dataId

      override def mapper: WordMapperPrediction = _mapper

      override def adjustOutputFunc: (Int, Double) => Double = _adjustOutput

      override def adjustOutput: Boolean = true

    }
    new CfgRaterAiFactory {
      override def description: String = s"Plain random ${_dataId}"

      override def shortDescription: String = s"PLAINR_${_dataId}"

      override def cfgRaterAi: () => CfgRaterAi = () => cfg
    }
  }


}
