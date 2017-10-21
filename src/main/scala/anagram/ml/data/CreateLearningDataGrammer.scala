package anagram.ml.data

object CreateLearningDataGrammer {

  private val wordList = WordList.loadWordListGrammer
  private val mapper = WordMapSingleWord.createWordMapperFromWordlist(wordList)

  val splitter = new BookSplitterTxt()
  val screator = new SentenceCreatorSliding()
  val srater = new SentenceRaterStraight(mapper)

  val creator = new CreateLearningData(mapper, splitter, screator, srater)

  def createData(dataId: String, bookCollection: BookCollection): Unit = {
    creator.createData(dataId, bookCollection)
  }

}
