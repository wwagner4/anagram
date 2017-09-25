package anagram.ml.train

object TrainingMain extends App {

  val firstRun = Run(
    id = "firstRun",
    dataId = "en02",
    desc = "Initial Tryout",
  )

  Training.train(firstRun)

}
