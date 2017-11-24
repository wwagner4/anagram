package anagram.model

import anagram.ml.data.common.{BookCollection, SentenceCreator, SentenceRater}
import anagram.words.WordMapper

trait CfgCreateDataFactory {

  def cfgCreateData: () => CfgCreateData
}

trait CfgCreateData {
  def id: String

  def mapper: WordMapper

  def sentenceCreator: SentenceCreator

  def sentenceRater: SentenceRater

  def bookCollection: BookCollection

  def sentenceLength: Iterable[Int]

  def adjustRating: (Double, Int) => Double

  def mapWordsToNumbers: Boolean
}
