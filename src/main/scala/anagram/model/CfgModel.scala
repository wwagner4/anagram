package anagram.model

trait SentenceLength {

  def length: Int

  def createDataOutputFactor: Double = 1.0

  def trainingIterations: Int

  def trainingBatchSize: Int

  def trainingLearningRate: Double

  def trainingIterationListenerUpdateCount: Int

  def ratingAdjustOutput: Double

  def id = s"$length"

  def desc = s"len:$length"

}

trait Cfg {

  def id: String

  def sentenceLengths: Iterable[SentenceLength]

}

trait CfgModel {

  def cfgCreateData: CfgCreateDataFactory

  def cfgTraining: CfgTrainingFactory

  def cfgRaterAi: CfgRaterAiFactory

}
