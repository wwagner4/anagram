package anagram.model.grmcat

import anagram.words._

class WordMapperFactoryGrammarCategorized(wl: Iterable[Word], grouperFactory: GrouperFactory) extends WordMapperFactory[Seq[String]] {

  def create: WordMapper[Seq[String]] = {

    lazy val grp = grouperFactory.grouper(wl)

    lazy val wmap = WordMapperHelper.toWordMap(wl)

    lazy val groups: Map[String, Int] = wl.map(w => grp.group(w.word)(0)).zipWithIndex.toMap

    new WordMapper[Seq[String]] {

      override def map(sentence: Seq[String]): MappingResult[Seq[String]] = {
        val inter = sentence.flatMap(w => grp.group(w))
        val features = inter.map(groups.getOrElse(_, 0).toDouble)
        MappingResult(
          intermediate = inter,
          features = features
        )
      }

      override def toWord(str: String): Option[Word] = wmap.get(str)

    }

  }


}
