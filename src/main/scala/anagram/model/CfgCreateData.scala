package anagram.model

import anagram.ml.data.common.{BookCollection, SentenceCreator, SentenceLabeler}
import anagram.words.WordMapper

trait CfgCreateDataFactory[T] {

  def cfgCreateData: () => CfgCreateData[T]

}

trait CfgCreateData[T] extends Cfg {

  def mapper: WordMapper[T]

  def sentenceCreator: SentenceCreator

  def sentenceLabeler: SentenceLabeler

  def bookCollection: BookCollection

}
