package anagram.model.plain

import anagram.common.{LinearAdjust, LinearAdjustParam}
import anagram.ml.data.common._
import anagram.model.{CfgCreateData, CfgModel, CfgRaterAi, CfgTraining}
import anagram.words.{WordMapper, WordMapperPrediction}

class CfgModelPlain extends CfgModel {

  val _dataId = "plain001"
  val _sentenceLengths = 2 to 5
  val _bookCollection = BookCollections.collectionEn2

  val _adjRating = List(
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

  val _mapper = WordMapperFactoryPlain.create
  val splitter = new BookSplitterTxt()
  val screator = new SentenceCreatorSliding()
  val srater = new SentenceRaterStraight(_mapper)

  override lazy val cfgCreateData = new CfgCreateData {

    override def id: String = _dataId
    override def adjustRating: (Double, Int) => Double = LinearAdjust.adjust(_adjRating)(_, _)
    override def sentenceLength: Iterable[Int] = _sentenceLengths
    override def mapper: WordMapper = _mapper
    override def sentenceCreator: SentenceCreator = screator
    override def sentenceRater: SentenceRater = srater
    override def bookCollection: BookCollection = _bookCollection
    override def mapWordsToNumbers: Boolean = true

  }

  override lazy val cfgTraining = new CfgTraining {

    override def id: String = _dataId
    override def iterations: Int => Int = (sentLen: Int) => {
      if (sentLen <= 2) 180
      else if (sentLen <= 3) 150
      else 120
    }

  }

  override lazy val cfgRaterAi = new CfgRaterAi {

    override def id: String = _dataId
    override def mapper: WordMapperPrediction = _mapper
    override def comonWordRating: Option[Double] = None
    override def adjustOutput: (Int, Double) => Double = adjustOutputPlain

  }

}
