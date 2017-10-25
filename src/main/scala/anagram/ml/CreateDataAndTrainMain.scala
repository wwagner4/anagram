package anagram.ml

import anagram.ml.data._
import anagram.ml.train.{Training, TrainingConfig}

object CreateDataAndTrainMain extends App {

  val id = "enGrm03"

  CreateLearningDataGrammar.createData(id, BookCollections.collectionEn2)

  val cfg = TrainingConfig(
    id,
    (sentLen: Int) => {
      if (sentLen == 2) 400
      else if (sentLen == 3) 300
      else if (sentLen == 4) 250
      else if (sentLen == 5) 100
      else throw new IllegalStateException("Unknown sentence length " + sentLen)
    },
  )
  Training.train(cfg)


}
