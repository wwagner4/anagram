package anagram.ml.data

object CreateLearningDataGrammer {

  private val mapper = WordMapGrammer.createWordMapperFull

  val splitter: BookSplitter = new BookSplitterTxt()
  val screator: SentenceCreator = new SentenceCreatorSliding()
  val srater: SentenceRater = DummyRater
  val creator = new CreateLearningData(mapper, splitter, screator, srater, mapWordsToNumbers = false)

  def createData(dataId: String, bookCollection: BookCollection): Unit = {
    creator.createData(dataId, bookCollection)
  }

}

object DummyRater extends SentenceRater {
  override def rateSentence(sentence: Sentence): Seq[Rated] = {
    Seq(Rated(sentence, 0.0))
  }
}