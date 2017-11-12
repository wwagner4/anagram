package anagram.ml.data.grmred

import anagram.common.{LinearAdjust, LinearAdjustParam}
import anagram.ml.data._
import anagram.words.WordMappers

object CreateLearningDataGrammarReduced {

  def createData(dataId: String): Unit = {

    val bookCollection: BookCollection = BookCollections.collectionEn2
    val sentLengths = (2 to 5).toList
    val mapWordsToNumbers = true

    val adjRating = List(
      (2, LinearAdjustParam(7469.6494,19744.8998)),
      (3, LinearAdjustParam(1050.2366,4166.9309)),
      (4, LinearAdjustParam(201.5350,1004.3174)),
      (5, LinearAdjustParam(50.9978,278.8994)),
    ).toMap

    val mapper = WordMappers.createWordMapperGrammerReduced
    val splitter = new BookSplitterTxt()
    val screator = new SentenceCreatorSliding()
    val srater = SentenceRaterCounting

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
