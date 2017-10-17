package anagram.ml

import anagram.ml.data._
import anagram.ml.train.Training

object CreateDataAndTrainMain extends App {

  val dataId = "en04"
  val createData = true

  val wm = WordMapSingleWord.createWordMapperFromWordlistResource("wordlist/wordlist_small.txt")

  val splitter = new BookSplitterTxt()
  val screator = new SentenceCreatorSliding()
  val srater = new SentenceRaterStraight(wm)

  if (createData) new LearningData(wm, splitter, screator, srater).createData(dataId, BookCollections.collectionEn2)
  Training.train(dataId)

}
