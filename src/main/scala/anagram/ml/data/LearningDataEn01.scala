package anagram.ml.data

import java.net.URI

object LearningDataEn01 extends App {

  val wm = WordMap.createWordMapFromWordlistResource("wordlist/wordlist_small.txt")
  val splitter = new BookSplitterTxt()
  val screator = new SentenceCreatorConditionalSliding()
  val srater = new SentenceRaterStraight(wm)

  new LearningData(wm, splitter, screator, srater).createData("en01", BookCollections.collectionEn1)

}
