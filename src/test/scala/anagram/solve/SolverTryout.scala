package anagram.solve

import java.nio.file.{Files, Path, Paths}

import anagram.common.{DataFile, IoUtil}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.util.ModelSerializer

import scala.collection.JavaConverters._


object SolverTryout extends App {

  val id = "en01"


  solve

  def loadNn: Unit = {
    val nnMap: Map[Int, MultiLayerNetwork] = {

      def deserializeNn(path: Path): MultiLayerNetwork = {
        println(s"-- ds: $path")
        ModelSerializer.restoreMultiLayerNetwork(path.toFile)
      }

      IoUtil.getNnDataFilesFromWorkDir(id)
        .map(df => (df.wordLen, deserializeNn(df.path)))
        .toMap
    }

    println(s"-- nnmap: ${nnMap.size}")
  }


  def solve: Unit = {
    val dict1 = IoUtil.getTxtFilePathFromWorkDir(s"${id}_dict")
    val dict2 = Paths.get(IoUtil.uri("wordlist/wordlist.txt"))

    val re = Solver.solve("bernd lives with ingrid in vienna", dict1)


    val atStart = System.currentTimeMillis()
    for ((sent, i) <- re.zipWithIndex) {
      println("%7d  -  %s".format(i, sent.mkString(" ")))
    }
    val atEnd = System.currentTimeMillis()

    println(s"-- duration: ${atEnd - atStart} ms")

  }

}

