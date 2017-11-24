package anagram.model

import anagram.words.WordMapperPrediction

trait  CfgRaterAiFactory {

  def description: String
  def shortDescription: String
  def cfgRaterAi: () => CfgRaterAi

}

trait CfgRaterAi{
  def id: String
  def comonWordRating: Option[Double]
  def mapper: WordMapperPrediction
  def adjustOutput: (Int, Double) => Double
}
