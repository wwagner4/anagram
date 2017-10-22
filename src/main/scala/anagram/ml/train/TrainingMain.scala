package anagram.ml.train

object TrainingMain extends App {

  val cfg = TrainingConfig(
    "en02",
    (sentLen: Int) => {
      if (sentLen <= 2 ) 180
      else if (sentLen <= 3 ) 150
      else 120
    },
  )

  Training.train(cfg)

}
