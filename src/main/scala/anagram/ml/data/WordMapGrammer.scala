package anagram.ml.data

import java.nio.file.Paths

import anagram.common.IoUtil

import scala.util.Random

class WordMapGrammer {

  case class Grp(grp: String, value: String)
  case class IdxGrp(index: Int, grp: String, value: String)

  def createWordMapperFromResource(resName: String): WordMapper = {

    val ran = Random

    def readLine(line: String): Grp = {
      val split = line.split(";")
      Grp(split(0), split(1))
    }

    val grps: Seq[Grp] = IoUtil.loadTxtFromPath(Paths.get(IoUtil.uri(resName)), (iter) => iter.toSeq.map(readLine))

    val idxGrps: Seq[IdxGrp] = for((grp, index) <- grps.zipWithIndex) yield IdxGrp(index, grp.grp, grp.value)

    val grpMap: Map[String, IdxGrp] = idxGrps.map(id => (id.value, id)).toMap

    val grpList = idxGrps.map(id => id.grp).distinct.sorted

    val grpListIdx = grpList.zipWithIndex

    val grpListWordMap: Map[String, Int] = grpListIdx.toMap
    val grpListIntMap: Map[Int, String] = grpListIdx.map{case (w, i) => (i, w)}.toMap

    new WordMapper {

      override def toNum(word: String): Int = grpListWordMap(word)

      override def toWord(num: Int): String = grpListIntMap(num)

      override def size: Int = grpListIdx.size

      override def randomWord: String = {
        val idx = ran.nextInt(grpListIdx.size)
        grpList(idx)
      }

      override def containsWord(str: String): Boolean = grpList.contains(str)

      override def group(value: String): String = grpMap(value).grp

    }

  }


}
