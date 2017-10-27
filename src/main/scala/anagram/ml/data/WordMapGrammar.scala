package anagram.ml.data

import java.nio.file.Paths

import anagram.common.IoUtil
import anagram.words.WordMapper

import scala.util.Random

// TODO Put together the creation of wordlist and mapper using the same resource file
object WordMapGrammar {

  case class GrpoupedWord(grp: String, value: String)

  def createWordMapperFull: WordMapper = {
    createWordMapperFromResource("wordlist/wordtypelist_full.txt")
  }

  def createWordMapperSmall: WordMapper = {
    createWordMapperFromResource("wordlist/wordtypelist_small.txt")
  }

  private def createWordMapperFromResource(resName: String): WordMapper = {

    val ran = Random

    def readLine(line: String): GrpoupedWord = {
      val split = line.split(";")
      GrpoupedWord(split(0), split(1))
    }

    val unknown = "?"

    val words: Seq[GrpoupedWord] = IoUtil.loadTxtFromPath(Paths.get(IoUtil.uri(resName)), (iter) => iter.toSeq.map(readLine))

    val wordMap: Map[String, GrpoupedWord] = words.map(gword => (gword.value, gword)).toMap

    val grpList = words.map(groupedWord =>  groupedWord.grp).distinct.sorted :+ unknown

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

      override def group(value: String): String = {
        wordMap.get(value).map(_.grp).getOrElse(unknown)
      }

    }

  }


}
