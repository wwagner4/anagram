package anagram.model

trait CfgTrainingFactory {

  def cfgTraining: () => CfgTraining

}

trait CfgTraining extends Cfg {

  def numberOfFeaturesForWord: Int = 1

}
