package anagram.ml

import anagram.ml.data._
import anagram.ml.train.Training

object CreateDataAndTrainMain extends App {

  val createData = true
  val id = "enGrm01"

  if (createData) CreateLearningDataGrammar.createData(id, BookCollections.collectionEn2)
  Training.train(id)


}
