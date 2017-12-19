package anagram.model.grmred

import anagram.model.plain.WordMapperFactoryPlain
import anagram.words.{Word, WordTransformer, Wordlists}

class WordTransformerGrammerReduced extends WordTransformer {

  private lazy val wl = WordMapperFactoryPlain.create.wordList

  private val unknown = "?"

  private val popularGrps = Set(
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

  private def reduceGroups(grp: String): String = {
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

  private val words: Seq[Word] = Wordlists.grammar.wordList().toSeq.map(w => w.copy(grp = Some(reduceGroups(w.grp.get))))
  private val wordMap: Map[String, Word] = words.map(gword => (gword.word, gword)).toMap

  private val grpList = words
    .map(groupedWord => groupedWord.grp.get)
    .distinct.sorted

  private val grpListIdx = grpList.zipWithIndex
  private val grpListWordMap: Map[String, Int] = grpListIdx.toMap

  override def transform(str: String): Seq[String] = Seq(wordMap.get(str).map(_.grp.get).getOrElse(unknown))

  override def toNum(str: String): Double = grpListWordMap.getOrElse(str, 0).toDouble

}
