package anagram.model

trait CfgTrainingFactory {

  def cfgTraining: () => CfgTraining

}

trait CfgTraining extends Cfg
