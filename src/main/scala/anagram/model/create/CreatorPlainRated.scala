package anagram.model.create

import anagram.ml.DataCollectorNull
import anagram.model.Configurations

object CreatorPlainRated extends AbstractCreator with App {

  val dc = new DataCollectorNull
  one(Configurations.plainRated, dc)

}
