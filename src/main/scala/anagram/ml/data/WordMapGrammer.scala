package anagram.ml.data

import java.nio.file.Paths

import anagram.common.IoUtil

class WordMapGrammer {

  case class Grp(grp: String, value: String)
  case class IdxGrp(index: Int, grp: String, value: String)

  def createWordMapperFromResource(resName: String): WordMapper = {

    def readLine(line: String): Grp = {
      val split = line.split(";")
      Grp(split(0), split(1))
    }

    val grps: Seq[Grp] = IoUtil.loadTxtFromPath(Paths.get(IoUtil.uri(resName)), (iter) => iter.toSeq.map(readLine))

    val idxGrps = for((grp, index) <- grps.zipWithIndex) yield IdxGrp(index, grp.grp, grp.value)

    new WordMapper {

      override def toNum(word: String): Int = ???

      override def toWord(num: Int): String = ???

      override def size: Int = ???

      override def randomWord: String = ???

      override def containsWord(str: String): Boolean = ???

      override def group(value: String) = ???
    }



  }


}
