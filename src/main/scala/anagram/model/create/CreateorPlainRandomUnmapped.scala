package anagram.model.create

import anagram.model.Configurations

object CreateorPlainRandomUnmapped extends AbstractCreator with App {

  unmapped(Configurations.plainRandom.cfgCreateData.cfgCreateData())

}

