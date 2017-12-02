package anagram.model.create

import anagram.ml.DataCollectorStdout
import anagram.model.Configurations

object CreatorPlain extends AbstractCreator with App {

  var dc = new DataCollectorStdout
  one(Configurations.plain, training = true, dc)
  dc.output()

}
