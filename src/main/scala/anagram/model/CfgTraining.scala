package anagram.model

trait CfgTrainingFactory {

  def cfgTraining: () => CfgTraining

}

trait CfgTraining extends Cfg {

  def id: String

  def batchSize: Int

  def learningRate: Double

  def iterationListenerUpdateCount: Int

}
