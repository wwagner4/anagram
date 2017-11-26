package anagram.model.create

import anagram.model.Configurations

object CreateorGrmUnmapped extends AbstractCreator with App {

  unmapped(Configurations.grammar.cfgCreateData.cfgCreateData())

}
