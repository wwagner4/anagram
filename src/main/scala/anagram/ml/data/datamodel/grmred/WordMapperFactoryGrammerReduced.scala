package anagram.ml.data.datamodel.grmred

import java.nio.file.Paths

import anagram.common.IoUtil
import anagram.ml.data.datamodel.plain.WordMapperFactoryPlain
import anagram.words.{Word, WordMapper, WordMapperFactory, WordMappersAbstract}

import scala.util.Random

object WordMapperFactoryGrammerReduced extends WordMappersAbstract with WordMapperFactory {

  case class GroupedWord(grp: String, value: String)

  val resName = "wordlist/wordtypelist_small.txt"

  def create: WordMapper = {
    val ran = Random

    lazy val wl = WordMapperFactoryPlain.create.wordList

    val unknown = "?"

    val popularGrps = Set(
      "n",
      "a",
      "vt",
      "adv",
      "adj",
      "npl",
      "vi",
      "propn",
      "pron",
      "prep",
    )

    def readLine(line: String): GroupedWord = {
      val split = line.split(";")
      GroupedWord(split(0), split(1))
    }

    def reduceGroups(grp: String): String = {
      // treatment for 'do' which is usually not a noun
      if (grp == "n&vt,auxiliary&vi") "vi"
      else {
        val i1 = grp.indexOf('&')
        val g1 = if (i1 >= 0) grp.substring(0, i1)
        else grp

        val i2 = g1.indexOf(',')
        val g2 = if (i2 >= 0) g1.substring(0, i2)
        else g1

        if (!popularGrps.contains(g1)) unknown
        else g2
      }
    }

    val words: Seq[GroupedWord] = IoUtil.loadTxtFromPath(
      Paths.get(IoUtil.uri(resName)),
      (iter) => iter.toSeq.map(readLine)).map(gw => GroupedWord(reduceGroups(gw.grp), gw.value))

    val wordMap: Map[String, GroupedWord] = words.map(gword => (gword.value, gword)).toMap

    val grpList = words
      .map(groupedWord => groupedWord.grp)
      .distinct.sorted

    val grpListIdx = grpList.zipWithIndex
    val grpListWordMap: Map[String, Int] = grpListIdx.toMap
    val grpListIntMap: Map[Int, String] = grpListIdx.map { case (w, i) => (i, w) }.toMap


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
