package anagram.model.create

import anagram.ml.DataCollectorStdout
import anagram.model.Configurations

object CreatorGrm extends AbstractCreator with App {

  val dc = new DataCollectorStdout
  one(
    Configurations.grammar,
    dc,
  )
  dc.output()

}
