package anagram.model.grm

import anagram.common.{LinearAdjust, LinearAdjustParam}
import anagram.ml.data.common._
import anagram.model._
import anagram.words.{WordMapper, WordMapperPrediction}

class CfgModelGrm extends CfgModel {

  private val _dataId = "grm001"
  private val _sentenceLengths = 2 to 5
  private lazy val _bookCollection = BookCollections.collectionEn2

  private lazy val _mapper = WordMapperFactoryGrammar.create

  private val screator = new SentenceCreatorSliding()
  private val srater = new SentenceRaterCounting
  private val _adjRating = List(
    (2, LinearAdjustParam(618.0639,2629.7913)),
    (3, LinearAdjustParam(64.0179,276.0944)),
    (4, LinearAdjustParam(11.5856,38.8528)),
    (5, LinearAdjustParam(3.5476,7.2595)),
  ).toMap

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

      override def batchSize: Int = 1000

      override def learningRate: Double = 0.00001

      override def iterations: Int => Int = (sentLen: Int) => {
        if (sentLen == 2) 500
        else if (sentLen == 3) 400
        else if (sentLen == 4) 350
        else if (sentLen == 5) 200
        else throw new IllegalStateException("Unknown sentence length " + sentLen)
      }
    }

    new CfgTrainingFactory {

      override def cfgTraining: () => CfgTraining = () => cfg
    }
  }

  private def _adjustOutput(len: Int, rating: Double): Double = {
    len match {
      case 1 => rating + 20
      case 2 => rating + 0.1848
      case 3 => rating + 0.0000
      case 4 => rating + 0.1224
      case 5 => rating + 0.6322
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

      override def description: String = s"Grammar ${_dataId}"

      override def shortDescription: String = s"GRM_${_dataId}"

      override def cfgRaterAi: () => CfgRaterAi = () => cfg

    }
  }


}
