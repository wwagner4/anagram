package anagram.model.create

import anagram.model.Configurations

object CreateorGrmRedUnmapped extends AbstractCreator with App {

  unmapped(Configurations.grammarReduced.cfgCreateData.cfgCreateData())

}
