package anagram.ml

import anagram.ml.data._
import anagram.ml.train.Training

object CreateDataAndTrainMain extends App {

  val id = "enGrm01"

  CreateLearningDataGrammer.createData(id, BookCollections.collectionTwoLines)

}
