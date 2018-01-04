package anagram.model.create

import anagram.ml.DataCollectorViz
import anagram.model.Configurations

object CreatorGrmCat extends AbstractCreator with App {

  val dc = new DataCollectorViz(s"grmCat_$timestamp", "Grammar categorized")
  one(Configurations.grammarCategorized, dc)
  dc.output()

}
