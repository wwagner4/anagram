package anagram.model.create

import anagram.ml.DataCollectorStdout

object CreatorAll extends AbstractCreator with App {

  val dc = new DataCollectorStdout
  all(dc)
  dc.output()
}

