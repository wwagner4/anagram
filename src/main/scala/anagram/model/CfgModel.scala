package anagram.model

sealed trait SentenceLength {

  def length: Int

  def additionalId: Option[String]

  def createDataOutputFactor: Double

  def trainingIterations: Int

  def trainingBatchSize: Int

  def trainingLearningRate: Double

  def trainingIterationListenerUpdateCount: Int

  def ratingAdjustOutput: Double

}

case class SentenceLength_2(
                             additionalId: Option[String] = None,
                             createDataOutputFactor: Double = 1.0,
                             trainingIterations: Int,
                             trainingBatchSize: Int,
                             trainingLearningRate: Double,
                             trainingIterationListenerUpdateCount: Int,
                             ratingAdjustOutput: Double,
                           ) extends SentenceLength {
  override def length: Int = 2

}

case class SentenceLength_3(
                             additionalId: Option[String] = None,
                             createDataOutputFactor: Double = 1.0,
                             trainingIterations: Int,
                             trainingBatchSize: Int,
                             trainingLearningRate: Double,
                             trainingIterationListenerUpdateCount: Int,
                             ratingAdjustOutput: Double,
                           ) extends SentenceLength {
  override def length: Int = 3

}

case class SentenceLength_4(
                             additionalId: Option[String] = None,
                             createDataOutputFactor: Double = 1.0,
                             trainingIterations: Int,
                             trainingBatchSize: Int,
                             trainingLearningRate: Double,
                             trainingIterationListenerUpdateCount: Int,
                             ratingAdjustOutput: Double,
                           ) extends SentenceLength {
  override def length: Int = 4

}

case class SentenceLength_5(
                             additionalId: Option[String] = None,
                             createDataOutputFactor: Double = 1.0,
                             trainingIterations: Int,
                             trainingBatchSize: Int,
                             trainingLearningRate: Double,
                             trainingIterationListenerUpdateCount: Int,
                             ratingAdjustOutput: Double,
                           ) extends SentenceLength {
  override def length: Int = 5

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
