package anagram.words

import anagram.ml.data.datamodel.grmred.WordMappersGrammerReduced

object WordMappers {

  def createWordMapperPlain: WordMapper = WordMappersPlain.createWordMapperPlain

  def createWordMapperGrammer: WordMapper = WordMappersGrammer.createWordMapperGrammer

  def createWordMapperGrammerReduced: WordMapper = WordMappersGrammerReduced.createWordMapper

}
