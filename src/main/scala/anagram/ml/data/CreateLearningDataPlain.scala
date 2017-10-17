package anagram.ml.data

import anagram.common.IoUtil

object CreateLearningDataPlain {

  val dataId = "en04"
  val createData = true

  val wordList = WordList.loadWordListSmall
  val mapper = WordMapSingleWord.createWordMapperFromWordlist(wordList)
  val grouper = WordGrouperIdentity

  val splitter = new BookSplitterTxt()
  val screator = new SentenceCreatorSliding()
  val srater = new SentenceRaterStraight(mapper)

  val creator = new CreateLearningData(mapper, grouper, splitter, screator, srater)

  def createData(dataId: String, bookCollection: BookCollection): Unit = {
    creator.createData(dataId, bookCollection)
  }

}
