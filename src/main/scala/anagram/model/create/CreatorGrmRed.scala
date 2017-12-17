package anagram.model.create

import anagram.ml.DataCollectorStdout
import anagram.model.Configurations

object CreatorGrmRed extends AbstractCreator with App {

  val dc = new DataCollectorStdout
  one(
    Configurations.grammarReduced,
    dc,
  )
  dc.output()

}
