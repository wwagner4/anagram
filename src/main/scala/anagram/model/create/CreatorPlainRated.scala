package anagram.model.create

import anagram.ml.DataCollectorStdout
import anagram.model.Configurations

object CreatorPlainRated extends AbstractCreator with App {

  val dc = new DataCollectorStdout
  one(Configurations.plainRated, dc)
  dc.output()

}
