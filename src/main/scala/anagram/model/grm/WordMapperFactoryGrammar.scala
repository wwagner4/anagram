package anagram.model.grm

import anagram.words.{Word, WordMapper, WordMapperFactory, Wordlists}

import scala.util.Random

object WordMapperFactoryGrammar extends WordMapperFactory {

  def create: WordMapper = {
    val ran = Random

    lazy val wl = Wordlists.grammar.wordList()

    lazy val wset = wl.map(_.word).toSet

    val unknown = "?"

    val words: Seq[Word] = wl.toSeq
    val wordMap: Map[String, Word] = words.map(gword => (gword.word, gword)).toMap

    val grpList = words.map(groupedWord => groupedWord.grp.get).distinct.sorted :+ unknown
    val grpListIdx = grpList.zipWithIndex
    val grpListWordMap: Map[String, Int] = grpListIdx.toMap

    new WordMapper {

      override def toNum(word: String): Int = grpListWordMap.getOrElse(word, 0)

      override def size: Int = grpListIdx.size

      override def randomWord: String = {
        val idx = ran.nextInt(grpListIdx.size)
        grpList(idx)
      }

      override def containsWord(str: String): Boolean = wset.contains(str)

      override def transform(value: String): Seq[String] =
        Seq(wordMap.get(value).map(_.grp.get).getOrElse(unknown))

      override def wordList: Iterable[Word] = wl

    }

  }

}
