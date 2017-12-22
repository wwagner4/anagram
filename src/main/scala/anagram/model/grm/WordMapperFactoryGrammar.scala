package anagram.model.grm

import anagram.words._

class WordMapperFactoryGrammar(wl: Iterable[Word]) extends WordMapperFactory[Seq[String]] {

  def create: WordMapper[Seq[String]] = {

    lazy val grp = new GrouperGrm(wl)

    lazy val wset = wl.map(_.word).toSet

    lazy val wmap = WordMapperHelper.toWordMap(wl)

    val unknown = "?"

    val words: Seq[Word] = wl.toSeq

    val grpList = words.map(groupedWord => groupedWord.grp.get).distinct.sorted :+ unknown
    val grpListIdx = grpList.zipWithIndex
    val grpListWordMap: Map[String, Int] = grpListIdx.toMap

    new WordMapper[Seq[String]] {

      override def map(sentence: Seq[String]): MappingResult[Seq[String]] = {
        val inter = sentence.flatMap(w => grp.group(w))
        val features = inter.map(grpListWordMap.getOrElse(_, 0).toDouble)
        MappingResult(
          intermediate = inter,
          features = features
        )
      }

      override def containsWord(str: String): Boolean = wset.contains(str)

      override def toWord(str: String): Option[Word] = wmap.get(str)

    }

  }

}
