package anagram.ml

trait DataCollector {

  def collectScore(sentenceLength: Int, iterations: Int, score: Double)

}
