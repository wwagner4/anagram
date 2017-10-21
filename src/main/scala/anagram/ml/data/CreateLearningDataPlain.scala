package anagram.ml.data

object CreateLearningDataPlain {

  val dataId = "en04"
  val createData = true

  private val wordList = WordList.loadWordListSmall
  private val mapper = WordMapSingleWord.createWordMapperFromWordlist(wordList)

  val splitter = new BookSplitterTxt()
  val screator = new SentenceCreatorSliding()
  val srater = new SentenceRaterStraight(mapper)

  val creator = new CreateLearningData(mapper, splitter, screator, srater)

  def createData(dataId: String, bookCollection: BookCollection): Unit = {
    creator.createData(dataId, bookCollection)
  }

}
