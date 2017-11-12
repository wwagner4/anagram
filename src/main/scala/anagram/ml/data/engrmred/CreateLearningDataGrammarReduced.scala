package anagram.ml.data.engrmred

import anagram.common.{LinearAdjust, LinearAdjustParam}
import anagram.ml.data._
import anagram.words.WordMappers

class CreateLearningDataGrammarReduced {

  def createData(dataId: String): Unit = {

    val bookCollection: BookCollection = BookCollections.collectionEn2
    val sentLengths = (2 to 5).toList
    val mapWordsToNumbers = true

    val adjRating = List(
      (2, LinearAdjustParam(0, 1)),
      (3, LinearAdjustParam(0, 1)),
      (4, LinearAdjustParam(0, 1)),
      (5, LinearAdjustParam(0, 1)),
    ).toMap

    val mapper = WordMappers.createWordMapperGrammerReduced
    val splitter = new BookSplitterTxt()
    val screator = new SentenceCreatorSliding()
    val srater = new SentenceRaterStraight(mapper)

    val creator = new CreateLearningData(mapper, splitter, screator, srater, mapWordsToNumbers)

    val cfg = CreateDataConfig(
      dataId,
      bookCollection,
      sentLengths,
      LinearAdjust.adjust(adjRating)(_, _),
    )

    creator.createData(cfg)
  }


}
