package anagram.ml.data

import java.net.URI
import java.nio.file.Paths

import anagram.common.IoUtil

object CreateLearningDataGrammer {

  val wordList = WordList.loadWordListGrammer
  val mapper = WordMapSingleWord.createWordMapperFromWordlist(wordList)

  val splitter = new BookSplitterTxt()
  val screator = new SentenceCreatorSliding()
  val srater = new SentenceRaterStraight(mapper)

  val creator = new CreateLearningData(mapper, splitter, screator, srater)

  def createData(dataId: String, bookCollection: BookCollection): Unit = {
    creator.createData(dataId, bookCollection)
  }

}
