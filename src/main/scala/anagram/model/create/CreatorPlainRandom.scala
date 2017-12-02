package anagram.model.create

import anagram.ml.DataCollectorStdout
import anagram.model.Configurations

object CreatorPlainRandom extends AbstractCreator with App {

  var dc = new DataCollectorStdout
  one(Configurations.plainRandom, training = true, dc)
  dc.output()

}
