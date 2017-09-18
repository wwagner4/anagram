package anagram.ml.train

import anagram.common.{DataFile, IoUtil}
import org.datavec.api.records.reader.impl.csv.CSVRecordReader
import org.datavec.api.split.FileSplit
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.{MultiLayerConfiguration, NeuralNetConfiguration, Updater}
import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.lossfunctions.LossFunctions
import org.slf4j.LoggerFactory

case class Run(
                id: String,
                dataId: String,
                desc: String,
              )


object Training {

  private val log = LoggerFactory.getLogger("Training")



  def train(run: Run): Unit = {
    log.info(s"Started training for run: '${run.id}'")
    IoUtil.getTxtDataFilesFromWorkDir(run.dataId).foreach{dataFile =>
      trainDataFile(dataFile)
    }
    log.info(s"Finished training for run: '${run.id}'")
  }

  def trainDataFile(dataFile: DataFile): Unit = {
    log.info(s"Started training data file: '${dataFile.path.getFileName}'")

    val recordReader = new CSVRecordReader(0, ";")
    recordReader.initialize(new FileSplit(dataFile.path.toFile))
    val dsIter = new RecordReaderDataSetIterator(recordReader, 1000, dataFile.wordLen, dataFile.wordLen, true)
    log.info(s"read dataset iterator: '${dsIter}'")
    val nnConf = nnConfiguration(dataFile.wordLen)
    val nn: MultiLayerNetwork = new MultiLayerNetwork(nnConf)
    nn.init()
    nn.setListeners(new ScoreIterationListener(100))
    log.info(s"started the training: '${nn}'")
    nn.fit(dsIter)
    log.info(s"Finished training data file: '${dataFile.path.getFileName}'")
  }

  private def nnConfiguration(numInput: Int): MultiLayerConfiguration = {

    val numHidden = 40

    new NeuralNetConfiguration.Builder()
      .seed(92384729384L)
      .iterations(1)
      .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
      .learningRate(0.00001)
      .weightInit(WeightInit.XAVIER)
      .updater(Updater.NESTEROVS)
      .regularization(false)
      .momentum(0.9)
      .list
      .layer(0, new DenseLayer.Builder()
        .nIn(numInput)
        .nOut(numHidden)
        .activation(Activation.TANH).build)
      .layer(1, new DenseLayer.Builder()
        .nIn(numHidden)
        .nOut(numHidden)
        .activation(Activation.TANH).build)
      .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
        .activation(Activation.IDENTITY).nIn(numHidden)
        .nOut(1)
        .build)
      .pretrain(false)
      .backprop(true)
      .build
  }

}
