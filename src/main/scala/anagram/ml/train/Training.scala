package anagram.ml.train

case class Run(
                id: String,
                dataId: String,
                desc: String,
                wordLength: Seq[Int],
              )


class Training {

  val firstRun = Run(
    id = "firstRun",
    dataId = "en01",
    desc = "Initial Tryout",
    wordLength = 2 to 6,
  )



}
