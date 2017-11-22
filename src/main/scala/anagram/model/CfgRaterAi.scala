package anagram.model

import anagram.words.WordMapperPrediction

trait CfgRaterAi{
  def id: String
  def comonWordRating: Option[Double]
  def mapper: WordMapperPrediction
  def adjustOutput: (Int, Double) => Double
}
