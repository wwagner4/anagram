package anagram.ml.data

object LearningDataEn01 extends App {

  val wm = WordMapSingleWord.createWordMapperFromWordlistResource("wordlist/wordlist_small.txt")
  val splitter = new BookSplitterTxt()
  val screator = new SentenceCreatorConditionalSliding()
  val srater = new SentenceRaterStraight(wm)

  new LearningData(wm, splitter, screator, srater).createData("en01", BookCollections.collectionEn1)

}
