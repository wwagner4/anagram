package anagram.model.create

import anagram.ml.DataCollectorViz

object CreatorAll extends AbstractCreator with App {

  val ts = timestamp

  private val dc: DataCollectorViz = DataCollectorViz(s"anaAllModels$ts", s"Anagram all models $ts")
  all(dc)
  dc.output()


}

