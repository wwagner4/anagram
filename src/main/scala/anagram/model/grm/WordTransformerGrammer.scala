package anagram.model.grm

import anagram.words.{Word, WordTransformer, Wordlists}

class WordTransformerGrammer extends WordTransformer {

  private val words: Seq[Word] = Wordlists.grammar.wordList().toSeq
  private val wordMap: Map[String, Word] = words.map(gword => (gword.word, gword)).toMap

  private val unknown = "?"

  private val grpList = words.map(groupedWord => groupedWord.grp.get).distinct.sorted :+ unknown
  private val grpListIdx = grpList.zipWithIndex
  private val grpListWordMap: Map[String, Int] = grpListIdx.toMap

  override def transform(str: String): Seq[String] = Seq(wordMap.get(str).map(_.grp.get).getOrElse(unknown))

  override def toNum(str: String): Double = grpListWordMap.getOrElse(str, 0).toDouble

}
