package anagram.model.grmred

import anagram.common.{LinearAdjust, LinearAdjustParam}
import anagram.ml.data.common._
import anagram.model.{CfgCreateData, CfgModel, CfgRaterAi, CfgTraining}
import anagram.words.{WordMapper, WordMapperPrediction}

class CfgModelGrmRed extends CfgModel {

  val _dataId = "grmRed001"
  val _sentenceLengths = 2 to 5
  val _bookCollection = BookCollections.collectionEn2

  val _mapper = WordMapperFactoryGrammerReduced.create

  val screator = new SentenceCreatorSliding()
  val srater = new SentenceRaterCounting
  val _adjRating = List(
    (2, LinearAdjustParam(7469.6494, 19744.8998)),
    (3, LinearAdjustParam(1050.2366, 4166.9309)),
    (4, LinearAdjustParam(201.5350, 1004.3174)),
    (5, LinearAdjustParam(50.9978, 278.8994)),
  ).toMap

  override lazy val cfgCreateData = new CfgCreateData {

    override def id: String = _dataId
    override def adjustRating: (Double, Int) => Double = LinearAdjust.adjust(_adjRating)(_, _)
    override def sentenceLength: Iterable[Int] = _sentenceLengths
    override def mapper: WordMapper = _mapper
    override def sentenceCreator: SentenceCreator = screator
    override def sentenceRater: SentenceRater = srater
    override def bookCollection: BookCollection = _bookCollection

  }

  override lazy val cfgTraining = new CfgTraining {

    override def id: String = _dataId
    override def iterations: Int => Int = (sentLen: Int) => {
      if (sentLen == 2) 600
      else if (sentLen == 3) 400
      else if (sentLen == 4) 300
      else if (sentLen == 5) 200
      else throw new IllegalStateException("Unknown sentence length " + sentLen)
    }

  }

  private def adjustOutputPlain(len: Int, rating: Double): Double = {
    len match {
      case 1 => rating + 20
      case 2 => rating + 0.6
      case 3 => rating + 0.2
      case 4 => rating + 0.3
      case 5 => rating
      case _ => rating - 20
    }
  }

  override lazy val cfgRaterAi = new CfgRaterAi {

    override def id: String = _dataId
    override def mapper: WordMapperPrediction = _mapper
    override def comonWordRating: Option[Double] = None
    override def adjustOutput: (Int, Double) => Double = adjustOutputPlain

  }

}
