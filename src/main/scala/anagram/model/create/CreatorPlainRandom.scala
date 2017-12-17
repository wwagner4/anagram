package anagram.model.create

import anagram.ml.DataCollectorStdout
import anagram.model.Configurations

object CreatorPlainRandom extends AbstractCreator with App {

  val dc = new DataCollectorStdout
  one(
    Configurations.plainRandom,
    dc
  )
  dc.output()

}
