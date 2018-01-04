package anagram.model

import anagram.words.WordMapperRating

trait CfgRaterAiFactory {

  def description: String

  def shortDescription: String

  def cfgRaterAi: () => CfgRaterAi

}

trait CfgRaterAi extends Cfg {

  def mapper: WordMapperRating[_]

  def adjustOutput: Boolean
}

