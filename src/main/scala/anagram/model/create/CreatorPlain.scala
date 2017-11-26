package anagram.model.create

import anagram.model.Configurations

object CreatorPlain extends AbstractCreator with App {

  one(Configurations.plainRandom)
  one(Configurations.plain)

}
