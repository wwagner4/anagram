package anagram.ml.data

import anagram.words.WordMappers

object CreateLearningDataPlain {

  val dataId = "en04"
  val createData = true

  private val mapper = WordMappers.createWordMapperPlain
  private val wordList = mapper.wordList

  val splitter = new BookSplitterTxt()
  val screator = new SentenceCreatorSliding()
  val srater = new SentenceRaterStraight(mapper)

  val creator = new CreateLearningData(mapper, splitter, screator, srater)

  def createData(dataId: String, bookCollection: BookCollection): Unit = {
    val cfg = CreateDataConfig(
      dataId,
      bookCollection,
      2 to 7,
      (rating: Double, _: Int) => rating // Don't change the rating
    )
    creator.createData(cfg)
  }

}
