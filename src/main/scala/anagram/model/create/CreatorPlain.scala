package anagram.model.create

import anagram.ml.DataCollectorNull
import anagram.model.Configurations

object CreatorPlain extends AbstractCreator with App {

  val dc = new DataCollectorNull
  one(
    Configurations.plain,
    dc,
  )

}
