package anagram.model.create

import anagram.model.Configurations

object CreateGrmRedUnmapped extends AbstractCreator with App {

  unmapped(Configurations.grammarReduced.cfgCreateData.cfgCreateData())

}
