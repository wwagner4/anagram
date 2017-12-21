package anagram.model.grm

import anagram.words.{Word, WordMapper, WordMapperFactory}

class WordMapperFactoryGrammar(wl: Iterable[Word]) extends WordMapperFactory {

  def create: WordMapper = {

    lazy val grp = new GrouperGrm(wl)

    lazy val wset = wl.map(_.word).toSet

    val unknown = "?"

    val words: Seq[Word] = wl.toSeq

    val grpList = words.map(groupedWord => groupedWord.grp.get).distinct.sorted :+ unknown
    val grpListIdx = grpList.zipWithIndex
    val grpListWordMap: Map[String, Int] = grpListIdx.toMap

    new WordMapper {

      override def map(sentence: Seq[String]): Seq[Double] = {
        sentence.flatMap(w => grp.group(w)).map(toNum(_).toDouble)
      }

      override def toNum(word: String): Int = grpListWordMap.getOrElse(word, 0)

      override def size: Int = grpListIdx.size

      override def containsWord(str: String): Boolean = wset.contains(str)

      override def wordList: Iterable[Word] = wl

    }

  }

}
