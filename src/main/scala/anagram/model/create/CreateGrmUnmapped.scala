package anagram.model.create

import anagram.model.Configurations

object CreateGrmUnmapped extends AbstractCreator with App {

  unmapped(Configurations.grammar.cfgCreateData.cfgCreateData())

}
