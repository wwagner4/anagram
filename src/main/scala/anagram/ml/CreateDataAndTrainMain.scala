package anagram.ml

import anagram.ml.data._

object CreateDataAndTrainMain extends App {

  val id = "enGrm01"

  CreateLearningDataGrammar.createData(id, BookCollections.collectionEn2)

}
