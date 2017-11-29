package anagram.model

trait CfgTrainingFactory {

  def cfgTraining: () => CfgTraining

}

trait CfgTraining {

  def id: String

  def batchSize: Int

  def learningRate: Double

  def iterationListenerUpdateCount: Int

  def iterations: Int => Int
}
