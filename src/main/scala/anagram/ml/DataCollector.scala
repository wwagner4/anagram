package anagram.ml

import anagram.model.SentenceLength

trait DataCollector {

  def collectScore(modelId: String, sentenceLength: SentenceLength, iterations: Int, score: Double): Unit

}
