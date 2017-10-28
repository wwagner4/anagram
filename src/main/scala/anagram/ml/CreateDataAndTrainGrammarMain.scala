package anagram.ml

import anagram.common.{LinearAdjust, LinearAdjustParam}
import anagram.ml.data._
import anagram.ml.train.{Training, TrainingConfig}
import anagram.words.WordMappers
import org.slf4j.LoggerFactory

object CreateDataAndTrainGrammarMain extends App {

  private val log = LoggerFactory.getLogger("CreateDataAndTrainGrammarMain")

  val id = "enGrm11"

  log.info(s"STARTED $id")

  CreateLearningDataGrammar.createData(id, BookCollections.collectionEn2)
  //TrainGrammar.train(id)

  log.info(s"FINISHED $id")

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


object CreateLearningDataGrammar {

  private val mapper = WordMappers.createWordMapperGrammer

  val splitter: BookSplitter = new BookSplitterTxt()
  val screator: SentenceCreator = new SentenceCreatorSliding()
  val srater: SentenceRater = SentenceRaterGrammar
  val creator = new CreateLearningData(mapper, splitter, screator, srater)

  private lazy val adjustments = List(
    (2, LinearAdjustParam(31.2029,229.6509)),
    (3, LinearAdjustParam(4.8286,23.7397)),
    (4, LinearAdjustParam(1.9724,4.4061)),
    (5, LinearAdjustParam(1.4014,1.5247)),
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

object SentenceRaterGrammar extends SentenceRater {
  override def rateSentence(sentences: Iterable[Sentence]): Iterable[Rated] = {
    val rmap: Seq[(Seq[String], Iterable[Sentence])] = sentences.groupBy(sent => sent.words).toSeq
    for ((w, sentences) <- rmap) yield {
      val y: Seq[(SentenceType, Int)] = sentences.map(_.sentenceType).groupBy(identity).mapValues(_.size).toSeq
      val rating: Double = y.foldLeft(0.0) {
        case (r, (stype, cnt)) => stype match {
          case SentenceType_COMPLETE => r + cnt * 10
          case SentenceType_BEGINNING => r + cnt * 5
          case SentenceType_OTHER => r + cnt * 1
        }
      }
      Rated(Sentence(SentenceType_OTHER, w), rating)
    }
  }
}