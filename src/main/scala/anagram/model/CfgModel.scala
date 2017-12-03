package anagram.model

sealed trait SentenceLength {

  def length: Int
  def trainingIterations: Int

}

case class SentenceLength_2(trainingIterations: Int) extends SentenceLength {
  override def length: Int = 2
}
case class SentenceLength_3(trainingIterations: Int) extends SentenceLength {
  override def length: Int = 3
}
case class SentenceLength_4(trainingIterations: Int) extends SentenceLength {
  override def length: Int = 4
}
case class SentenceLength_5(trainingIterations: Int) extends SentenceLength {
  override def length: Int = 5
}

trait CfgModel {

  def cfgCreateData: CfgCreateDataFactory

  def cfgTraining: CfgTrainingFactory

  def cfgRaterAi: CfgRaterAiFactory

}
