package anagram.words

import java.nio.file.Paths

import anagram.common.IoUtil

import scala.util.Random

object WordMappersGrammer extends WordMappersAbstract {

  case class GrpoupedWord(grp: String, value: String)

  val resName = "wordlist/wordtypelist_small.txt"

  def createWordMapperGrammer: WordMapper = {
    val ran = Random

    def readLine(line: String): GrpoupedWord = {
      val split = line.split(";")
      GrpoupedWord(split(0), split(1))
    }

    lazy val wl = WordMappersPlain.createWordMapperPlain.wordList

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

      override def transform(value: String): Seq[String] =
        Seq(wordMap.get(value).map(_.grp).getOrElse(unknown))

      override def wordList: Iterable[Word] = wl

    }

  }

}
