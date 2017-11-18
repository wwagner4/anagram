package anagram.ml.data.datamodel.grmred

import anagram.ml.train.{Training, TrainingConfig}

object GrammarReducedMain extends App {

  val _id = "GrmRed01"
  CreateLearningDataGrammarReduced.createData(_id)
  trainDataGrammarReduced

  def trainDataGrammarReduced = {

    val cfg = TrainingConfig(
      id = _id,
      iterations = (sentLen: Int) => {
        if (sentLen == 2) 600
        else if (sentLen == 3) 400
        else if (sentLen == 4) 300
        else if (sentLen == 5) 200
        else throw new IllegalStateException("Unknown sentence length " + sentLen)
      },
    )
    Training.train(cfg)
  }

}
