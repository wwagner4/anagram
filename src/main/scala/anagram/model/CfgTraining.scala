package anagram.model

trait CfgTrainingFactory {

  def cfgTraining: () => CfgTraining

}

trait CfgTraining {

  def id: String

  def batchSize: Int

  def iterations: Int => Int
}
