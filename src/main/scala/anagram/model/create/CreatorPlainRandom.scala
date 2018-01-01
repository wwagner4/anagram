package anagram.model.create

import anagram.ml.DataCollectorNull
import anagram.model.Configurations

object CreatorPlainRandom extends AbstractCreator with App {

  val dc = new DataCollectorNull
  one(
    Configurations.plainRandom,
    dc
  )

}
