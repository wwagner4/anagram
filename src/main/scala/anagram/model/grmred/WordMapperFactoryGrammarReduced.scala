package anagram.model.grmred

import anagram.words._

class WordMapperFactoryGrammarReduced(wl: Iterable[Word]) extends WordMapperFactory[Seq[String]] {

  def create: WordMapper[Seq[String]] = {

    lazy val wl = Wordlists.grammar.wordList()

    lazy val wset = wl.map(_.word).toSet

    lazy val grp: Grouper = new GrouperGrmRed(wl)

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
      "prep"
    )

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

    val words: Seq[Word] = wl.toSeq.map(w => w.copy(grp = Some(reduceGroups(w.grp.get))))

    val grpList = words
      .map(groupedWord => groupedWord.grp.get)
      .distinct.sorted

    val grpListIdx = grpList.zipWithIndex
    val grpListWordMap: Map[String, Int] = grpListIdx.toMap

    new WordMapper[Seq[String]] {

      override def map(sentence: Seq[String]): MappingResult[Seq[String]] = {
        val inter = sentence.flatMap(w => grp.group(w))
        val feat = inter.map(toNum(_).toDouble)
        MappingResult(
          intermediate = inter,
          features = feat,
        )
      }

      private def toNum(word: String): Int = grpListWordMap.getOrElse(word, 0)

      override def size: Int = grpListIdx.size

      override def containsWord(str: String): Boolean = wset.contains(str)

      override def wordList: Iterable[Word] = wl

    }

  }

}
