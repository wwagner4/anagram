package anagram.ml

import anagram.common.{LinearAdjust, LinearAdjustParam}
import anagram.ml.data._
import anagram.ml.train.{Training, TrainingConfig}
import anagram.words.WordMappers
import org.slf4j.LoggerFactory

object CreateDataAndTrainPlainMain extends App {

  private val log = LoggerFactory.getLogger("CreateDataAndTrainPlainMain")

  val dataId = "enPlain11"
  private val coll: BookCollection = BookCollections.collectionEn2

  log.info(s"STARTED $dataId")

  CreateLearningData.createData(dataId, coll)
  //TrainingPlain.train(dataId)

  log.info(s"FINISHED $dataId")

}

object TrainingPlain {

  def train(id: String): Unit = {
    val cfg = TrainingConfig(
      id,
      (sentLen: Int) => {
        if (sentLen <= 2 ) 180
        else if (sentLen <= 3 ) 150
        else 120
      },
    )
    Training.train(cfg)
  }

}

object CreateLearningData {

  def createData(dataId: String, bookCollection: BookCollection): Unit = {

    val adjRating = List(
      (2, LinearAdjustParam(10.1645,3.3469)),
      (3, LinearAdjustParam(9.9123,3.4459)),
      (4, LinearAdjustParam(9.8736,3.5693)),
      (5, LinearAdjustParam(9.9484,3.7168)),
    ).toMap

    val mapper = WordMappers.createWordMapperPlain
    val splitter = new BookSplitterTxt()
    val screator = new SentenceCreatorSliding()
    val srater = new SentenceRaterStraight(mapper)

    val creator = new CreateLearningData(mapper, splitter, screator, srater)

    val cfg = CreateDataConfig(
      dataId,
      bookCollection,
      2 to 5,
      LinearAdjust.adjust(adjRating)(_, _),
    )

    creator.createData(cfg)
  }

}
