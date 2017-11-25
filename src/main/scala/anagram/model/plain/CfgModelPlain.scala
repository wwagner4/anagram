package anagram.model.plain

import anagram.common.{LinearAdjust, LinearAdjustParam}
import anagram.ml.data.common._
import anagram.model._
import anagram.words.{WordMapper, WordMapperPrediction}

class CfgModelPlain extends CfgModel {

  private val _dataId = "plain001"
  private val _sentenceLengths = 2 to 5
  private lazy val _bookCollection = BookCollections.collectionEn2

  private val _adjRating = List(
    (2, LinearAdjustParam(21.7562, 7.1637)),
    (3, LinearAdjustParam(21.9250, 7.6221)),
    (4, LinearAdjustParam(22.0688, 7.9778)),
    (5, LinearAdjustParam(22.1913, 8.2909)),
  ).toMap

  private def adjustOutputPlain(len: Int, rating: Double): Double = {
    if (len == 1) rating + 5 // Anagram existing of one word must always be top
    else if (len == 2) rating + 3.9
    else if (len == 3) rating + 1.5
    else if (len == 4) rating + 1.2
    else rating
  }

  private lazy val _mapper = WordMapperFactoryPlain.create
  val splitter = new BookSplitterTxt()
  val screator = new SentenceCreatorSliding()
  lazy val srater = new SentenceRaterStraight(_mapper)

  override lazy val cfgCreateData: CfgCreateDataFactory = {    lazy val cfg = new CfgCreateData {

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

      override def iterations: Int => Int = (sentLen: Int) => {
        if (sentLen <= 2) 180
        else if (sentLen <= 3) 150
        else 120
      }

    }
    new CfgTrainingFactory {
      override def cfgTraining: () => CfgTraining = () => cfg
    }
  }


  override lazy val cfgRaterAi: CfgRaterAiFactory = {
    lazy val cfg = new CfgRaterAi {

      override def id: String = _dataId

      override def mapper: WordMapperPrediction = _mapper

      override def comonWordRating: Option[Double] = None

      override def adjustOutput: (Int, Double) => Double = adjustOutputPlain

    }
    new CfgRaterAiFactory {override def description: String = s"Plain ${_dataId}"

      override def shortDescription: String = s"PLAIN_${_dataId}"

      override def cfgRaterAi: () => CfgRaterAi = () => cfg
    }
  }




}
