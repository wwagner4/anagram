package anagram.model.create

import anagram.ml.DataCollectorStdout

object CreatorAll extends AbstractCreator with App {

  var dc = new DataCollectorStdout
  all(training = true, dc)
  dc.output()
}

