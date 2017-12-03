package anagram.ml.train

import java.nio.file.Path

import anagram.common.IoUtil
import anagram.common.IoUtil.dirWork
import anagram.ml.{DataCollector, MlUtil}
import anagram.model.{CfgTraining, SentenceLength}
import org.datavec.api.records.reader.impl.csv.CSVRecordReader
import org.datavec.api.split.FileSplit
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.conf.{MultiLayerConfiguration, NeuralNetConfiguration, Updater}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.util.ModelSerializer
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.lossfunctions.LossFunctions
import org.slf4j.LoggerFactory

import scala.util.Random


case class CfgTrainingImpl(
                            sentenceLengths: Iterable[SentenceLength],
                            id: String,
                            batchSize: Int,
                            learningRate: Double,
                            iterationListenerUpdateCount: Int,
                            iterations: Int => Int
                          ) extends CfgTraining

object Training {

  private val log = LoggerFactory.getLogger("Training")

  def train(cfg: CfgTraining, dataCollector: DataCollector): Unit = {
    for (s <- cfg.sentenceLengths) {
      train(cfg, s, dataCollector)
    }
  }

  def train(cfg: CfgTraining, sentenceLength: SentenceLength, dataCollector: DataCollector): Unit = {
    log.info(s"Started training for run: '${cfg.id}'")
    val dataFile = IoUtil.dirWork.resolve(MlUtil.dataFileName(cfg.id, sentenceLength.length))
    trainDataFile(dataFile, cfg, sentenceLength, dataCollector)
    log.info(s"Finished training for run: '${cfg.id}'")
  }

  def trainDataFile(dataFile: Path, cfg: CfgTraining, sentenceLength: SentenceLength, dataCollector: DataCollector): Unit = {
    log.info(s"Started training data file: '${dataFile.getFileName}'")

    val recordReader = new CSVRecordReader(0, ';')
    recordReader.initialize(new FileSplit(dataFile.toFile))
    val dsIter = new RecordReaderDataSetIterator(recordReader, cfg.batchSize, sentenceLength.length, sentenceLength.length, true)
    log.info(s"read dataset iterator")
    val nnConf = nnConfiguration(
      sentenceLength.length,
      sentenceLength.trainingIterations,
      cfg.learningRate
    )
    val nn: MultiLayerNetwork = new MultiLayerNetwork(nnConf)
    nn.init()
    val listenerScore = new IterationListenerScore(dataCollector, sentenceLength.length, cfg)
    nn.setListeners(listenerScore)
    log.info(s"started the training")
    nn.fit(dsIter)

    val serfile = nnDataFilePath(cfg.id, sentenceLength.length)
    ModelSerializer.writeModel(nn, serfile.toFile, true)
    log.info(s"Wrote net to: '$serfile'")

    log.info(s"Finished training data file: '${dataFile.getFileName}'")
  }

  private def nnConfiguration(numInput: Int, iterations: Int, learningRate: Double): MultiLayerConfiguration = {

    log.info(s"numInput: $numInput")
    log.info(s"iterations: $iterations")
    log.info(f"learningRate: $learningRate%.2e")

    val numHidden = 400
    val act = Activation.SIGMOID

    new NeuralNetConfiguration.Builder()
      .seed(Random.nextLong())
      .iterations(iterations)
      .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
      .learningRate(learningRate)
      .weightInit(WeightInit.XAVIER)
      .updater(Updater.NESTEROVS)
      .regularization(false)
      .list
      .layer(0, new DenseLayer.Builder()
        .nIn(numInput)
        .nOut(numHidden)
        .activation(act).build)
      .layer(1, new DenseLayer.Builder()
        .nIn(numHidden)
        .nOut(numHidden)
        .activation(act).build)
      .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
        .activation(Activation.IDENTITY).nIn(numHidden)
        .nOut(1)
        .build)
      .pretrain(false)
      .backprop(true)
      .build
  }

  private def nnDataFilePath(id: String, sentenceLength: Int): Path = {
    dirWork.resolve(s"anagram_${id}_nn_$sentenceLength.ser")
  }

}
