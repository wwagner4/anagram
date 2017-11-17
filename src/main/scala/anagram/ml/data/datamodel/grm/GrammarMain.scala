package anagram.ml.data.datamodel.grm

import anagram.common.{LinearAdjust, LinearAdjustParam}
import anagram.ml.data.common._
import anagram.ml.train.{Training, TrainingConfig}
import org.slf4j.LoggerFactory

object GrammarMain extends App {

  private val log = LoggerFactory.getLogger("GrammarMain")

  val nr = "12"

  createAndTrainGrammar()

  def createAndTrainGrammar(): Unit = {
    val id = s"enGrm$nr"
    log.info(s"STARTED $id")
    CreateLearningDataGrammar.createData(id, BookCollections.collectionEn2)
    TrainGrammar.train(id)
    log.info(s"FINISHED $id")
  }

}

object CreateLearningDataGrammar {

  private val mapper = WordMappersGrammer.createWordMapper

  val splitter: BookSplitter = new BookSplitterTxt()
  val screator: SentenceCreator = new SentenceCreatorSliding()
  val srater: SentenceRater = SentenceRaterCounting
  val creator = new CreateLearningData(mapper, splitter, screator, srater)

  private lazy val adjustments = List(
    (2, LinearAdjustParam(31.2029, 229.6509)),
    (3, LinearAdjustParam(4.8286, 23.7397)),
    (4, LinearAdjustParam(1.9724, 4.4061)),
    (5, LinearAdjustParam(1.4014, 1.5247)),
  ).toMap

  def createData(dataId: String, bookCollection: BookCollection): Unit = {
    val cfg = CreateDataConfig(
      dataId,
      bookCollection,
      2 to 5,
      LinearAdjust.adjust(adjustments)(_, _),
    )
    creator.createData(cfg)
  }

}

object TrainGrammar {

  def train(id: String): Unit = {
    val cfg = TrainingConfig(
      id,
      (sentLen: Int) => {
        if (sentLen == 2) 400
        else if (sentLen == 3) 300
        else if (sentLen == 4) 250
        else if (sentLen == 5) 100
        else throw new IllegalStateException("Unknown sentence length " + sentLen)
      },
    )
    Training.train(cfg)
  }
}

