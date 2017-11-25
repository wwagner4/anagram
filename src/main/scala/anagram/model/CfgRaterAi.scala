package anagram.model

import anagram.words.WordMapperPrediction

trait CfgRaterAiFactory {

  def description: String

  def shortDescription: String

  def cfgRaterAi: () => CfgRaterAi

}

trait CfgRaterAi {

  def id: String

  def mapper: WordMapperPrediction

  def adjustOutputFunc: (Int, Double) => Double

  def adjustOutput: Boolean
}

case class CfgRaterAiImpl(
                           id: String,
                           mapper: WordMapperPrediction,
                           adjustOutputFunc: (Int, Double) => Double,
                           adjustOutput: Boolean,
                         ) extends CfgRaterAi {
  def create(cfg: CfgRaterAi): CfgRaterAiImpl = {
    CfgRaterAiImpl(
      cfg.id,
      cfg.mapper,
      cfg.adjustOutputFunc,
      cfg.adjustOutput,
    )
  }
}

object CfgRaterAiImplCreator {

  def create(cfg: CfgRaterAi): CfgRaterAiImpl = {
    CfgRaterAiImpl(
      cfg.id,
      cfg.mapper,
      cfg.adjustOutputFunc,
      cfg.adjustOutput,
    )
  }
}