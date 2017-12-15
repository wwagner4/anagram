package anagram.model

import anagram.words.WordMapperRating

trait CfgRaterAiFactory {

  def description: String

  def shortDescription: String

  def cfgRaterAi: () => CfgRaterAi

}

trait CfgRaterAi extends Cfg {

  def mapper: WordMapperRating

  def adjustOutput: Boolean
}

case class CfgRaterAiImpl(
                           sentenceLengths: Iterable[SentenceLength],
                           id: String,
                           mapper: WordMapperRating,
                           adjustOutput: Boolean,
                         ) extends CfgRaterAi {
  def create(cfg: CfgRaterAi): CfgRaterAiImpl = {
    CfgRaterAiImpl(
      cfg.sentenceLengths,
      cfg.id,
      cfg.mapper,
      cfg.adjustOutput,
    )
  }

}

object CfgRaterAiImplCreator {

  def create(cfg: CfgRaterAi): CfgRaterAiImpl = {
    CfgRaterAiImpl(
      cfg.sentenceLengths,
      cfg.id,
      cfg.mapper,
      cfg.adjustOutput,
    )
  }
}