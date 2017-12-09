package anagram.ml

import anagram.model.SentenceLength

trait DataCollector {

  def collectScore(sentenceLength: SentenceLength, iterations: Int, score: Double)

}
