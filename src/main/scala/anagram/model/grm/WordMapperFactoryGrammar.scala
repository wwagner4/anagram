package anagram.model.grm

import anagram.words.{MappingResult, Word, WordMapper, WordMapperFactory}

class WordMapperFactoryGrammar(wl: Iterable[Word]) extends WordMapperFactory[Seq[String]] {

  def create: WordMapper[Seq[String]] = {

    lazy val grp = new GrouperGrm(wl)

    lazy val wset = wl.map(_.word).toSet

    val unknown = "?"

    val words: Seq[Word] = wl.toSeq

    val grpList = words.map(groupedWord => groupedWord.grp.get).distinct.sorted :+ unknown
    val grpListIdx = grpList.zipWithIndex
    val grpListWordMap: Map[String, Int] = grpListIdx.toMap

    new WordMapper[Seq[String]] {

      override def map(sentence: Seq[String]): MappingResult[Seq[String]] = {
        val inter = sentence.flatMap(w => grp.group(w))
        val features = inter.map(toNum(_).toDouble)
        MappingResult(
          intermediate = inter,
          features = features,
        )
      }

      override def toNum(word: String): Int = grpListWordMap.getOrElse(word, 0)

      override def size: Int = grpListIdx.size

      override def containsWord(str: String): Boolean = wset.contains(str)

      override def wordList: Iterable[Word] = wl

    }

  }

}
