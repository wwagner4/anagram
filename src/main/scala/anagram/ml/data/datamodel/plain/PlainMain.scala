package anagram.ml.data.datamodel.plain

import anagram.common.{LinearAdjust, LinearAdjustParam}
import anagram.ml.data.common._
import anagram.ml.train.{Training, TrainingConfig}
import org.slf4j.LoggerFactory

object PlainMain extends App {

  private val log = LoggerFactory.getLogger("PlainMain")

  val nr = "12"

  createAndTrainPlain()

  def createAndTrainPlain(): Unit = {
    val id = s"enPlain$nr"
    log.info(s"STARTED $id")
    CreateLearningDataPlain.createData(id, BookCollections.collectionEn2)
    TrainPlain.train(id)
    log.info(s"FINISHED $id")
  }


}

object CreateLearningDataPlain {

  def createData(dataId: String, bookCollection: BookCollection): Unit = {

    val adjRating = List(
      (2, LinearAdjustParam(21.7562, 7.1637)),
      (3, LinearAdjustParam(21.9250, 7.6221)),
      (4, LinearAdjustParam(22.0688, 7.9778)),
      (5, LinearAdjustParam(22.1913, 8.2909)),
    ).toMap

    val mapper = WordMapperFactoryPlain.create
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

object TrainPlain {

  def train(id: String): Unit = {
    val cfg = TrainingConfig(
      id,
      (sentLen: Int) => {
        if (sentLen <= 2) 180
        else if (sentLen <= 3) 150
        else 120
      },
    )
    Training.train(cfg)
  }

}

