package anagram.ml.train

object TrainingMain extends App {

  val firstRun = Run(
    id = "firstRun",
    dataId = "en01",
    desc = "Initial Tryout",
  )

  Training.train(firstRun)

}
