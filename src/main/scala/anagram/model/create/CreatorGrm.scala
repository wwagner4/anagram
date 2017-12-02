package anagram.model.create

import anagram.ml.DataCollectorStdout
import anagram.model.Configurations

object CreatorGrm extends AbstractCreator with App {

  var dc = new DataCollectorStdout
  one(Configurations.grammar, training = true, dc)
  dc.output()

}
