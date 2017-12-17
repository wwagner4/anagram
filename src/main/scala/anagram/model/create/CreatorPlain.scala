package anagram.model.create

import anagram.ml.DataCollectorStdout
import anagram.model.Configurations

object CreatorPlain extends AbstractCreator with App {

  val dc = new DataCollectorStdout
  one(
    Configurations.plain,
    dc,
  )
  dc.output()

}
