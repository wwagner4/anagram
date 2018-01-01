package anagram.ml
import anagram.model.SentenceLength

// Does not collect anything
class DataCollectorNull extends DataCollector {

  override def collectScore(sentenceLength: SentenceLength, iterations: Int, score: Double): Unit = ()

  override def nextModel(name: String): Unit = ()

}
