package anagram.model

import anagram.ml.data.common.{BookCollection, SentenceCreator, SentenceLabeler}
import anagram.words.WordMapper

trait CfgCreateDataFactory {

  def cfgCreateData: () => CfgCreateData
}

trait CfgCreateData extends Cfg {

  def mapper: WordMapper

  def sentenceCreator: SentenceCreator

  def sentenceLabeler: SentenceLabeler

  def bookCollection: BookCollection

}
