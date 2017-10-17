package anagram.ml

import anagram.common.IoUtil
import anagram.ml.data._
import anagram.ml.train.Training

object CreateDataAndTrainMain extends App {

  val dataId = "en04"
  val createData = true

  val wordList = IoUtil.loadWordList("wordlist/wordlist_small.txt")
  val mapper = WordMapSingleWord.createWordMapperFromWordlist(wordList)
  val grouper = WordGrouperIdentity

  val splitter = new BookSplitterTxt()
  val screator = new SentenceCreatorSliding()
  val srater = new SentenceRaterStraight(mapper)

  if (createData) new LearningData(mapper, grouper, splitter, screator, srater).createData(dataId, BookCollections.collectionEn2)
  Training.train(dataId)

}
