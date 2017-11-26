package anagram.model.create

import anagram.model.Configurations

object CreateorPlainUnmapped extends AbstractCreator with App {

  unmapped(Configurations.plain.cfgCreateData.cfgCreateData())

}
