package anagram.ml

import anagram.ml.data._
import anagram.ml.train.{Training, TrainingConfig}

object CreateDataAndTrainMain extends App {

  val createData = false
  val id = "enGrm01"

  if (createData) CreateLearningDataGrammar.createData(id, BookCollections.collectionEn2)
  val cfg = TrainingConfig(
    id,
    (sentLen: Int) => {
      if (sentLen == 2) 200
      else if (sentLen == 3) 200
      else if (sentLen == 4) 200
      else if (sentLen == 5) 200
      else if (sentLen == 6) 200
      else if (sentLen == 7) 200
      else throw new IllegalStateException("Unknown sentence length " + sentLen)
    },
  )
  Training.train(cfg)


}
