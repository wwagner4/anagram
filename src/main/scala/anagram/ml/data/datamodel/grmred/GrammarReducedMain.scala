package anagram.ml.data.datamodel.grmred

import anagram.ml.train.{Training, TrainingConfig}

object GrammarReducedMain extends App {

  val id = "GrmRed01"
  CreateLearningDataGrammarReduced.createData(id)
  trainDataGrammarReduced(id)

  def trainDataGrammarReduced(id: String) = {

    val cfg = TrainingConfig(
      id,
      (sentLen: Int) => {
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
