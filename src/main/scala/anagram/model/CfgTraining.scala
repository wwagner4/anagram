package anagram.model

trait CfgTrainingFactory {

  def cfgTraining: () => CfgTraining

}

trait CfgTraining {

  def id: String

  def iterations: Int => Int
}
