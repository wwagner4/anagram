package anagram.model.grm

import anagram.model.plain.WordMapperFactoryPlain
import anagram.words.{Word, WordMapper, WordMapperFactory, Wordlists}

import scala.util.Random

object WordMapperFactoryGrammar extends WordMapperFactory {

  def create: WordMapper = {
    val ran = Random

    lazy val wl = WordMapperFactoryPlain.create.wordList

    val unknown = "?"

    val words: Seq[Word] = Wordlists.grammar.toSeq
    val wordMap: Map[String, Word] = words.map(gword => (gword.word, gword)).toMap

    val grpList = words.map(groupedWord => groupedWord.grp.get).distinct.sorted :+ unknown
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
        Seq(wordMap.get(value).map(_.grp.get).getOrElse(unknown))

      override def wordList: Iterable[Word] = wl

    }

  }

}
