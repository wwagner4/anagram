package anagram.ml.train

import java.nio.file.Path

import anagram.common.IoUtil.dirWork
import anagram.common.{DataFile, IoUtil}
import anagram.ml.MlUtil
import anagram.model.CfgTraining
import org.datavec.api.records.reader.impl.csv.CSVRecordReader
import org.datavec.api.split.FileSplit
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.conf.{MultiLayerConfiguration, NeuralNetConfiguration, Updater}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.deeplearning4j.util.ModelSerializer
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.lossfunctions.LossFunctions
import org.slf4j.LoggerFactory


case class CfgTrainingImpl(
                         id: String,
                         iterations: Int => Int
                         ) extends CfgTraining

object Training {

  private val log = LoggerFactory.getLogger("Training")

  def train(cfg: CfgTraining): Unit = {
    log.info(s"Started training for run: '${cfg.id}'")
    MlUtil.getTxtDataFilesFromWorkDir(IoUtil.dirWork, cfg.id).foreach{dataFile =>
      trainDataFile(dataFile, cfg)
    }
    log.info(s"Finished training for run: '${cfg.id}'")
  }

  def trainDataFile(dataFile: DataFile, cfg: CfgTraining): Unit = {
    log.info(s"Started training data file: '${dataFile.path.getFileName}'")

    val recordReader = new CSVRecordReader(0, ';')
    recordReader.initialize(new FileSplit(dataFile.path.toFile))
    val dsIter = new RecordReaderDataSetIterator(recordReader, 100000, dataFile.wordLen, dataFile.wordLen, true)
    log.info(s"read dataset iterator")
    val nnConf = nnConfiguration(dataFile.wordLen, cfg.iterations(dataFile.wordLen))
    val nn: MultiLayerNetwork = new MultiLayerNetwork(nnConf)
    nn.init()
    nn.setListeners(new ScoreIterationListener(30))
    log.info(s"started the training")
    nn.fit(dsIter)

    val serfile = nnDataFilePath(cfg.id, dataFile.wordLen)
    ModelSerializer.writeModel(nn, serfile.toFile, true)
    log.info(s"Wrote net to: '$serfile'")

    log.info(s"Finished training data file: '${dataFile.path.getFileName}'")
  }

  private def nnConfiguration(numInput: Int, iterations: Int): MultiLayerConfiguration = {

    val numHidden = 400
    val act = Activation.SIGMOID

    new NeuralNetConfiguration.Builder()
      .seed(92388784L)
      .iterations(iterations)
      .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
      .learningRate(0.00001)
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
