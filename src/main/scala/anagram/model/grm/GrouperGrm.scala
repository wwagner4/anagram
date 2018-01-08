package anagram.model.grm

import anagram.words.{Grouper, Word}

class GrouperGrm(wl: Iterable[Word]) extends Grouper {

  val words: Seq[Word] = wl.toSeq
  val wordMap: Map[String, Word] = words.map(gword => (gword.word, gword)).toMap

  val unknown = "?"

  override def group(value: String): Seq[String] =
    Seq(wordMap.get(value).map(_.grp.get).getOrElse(unknown))

  override def groups: Seq[String] = throw new IllegalStateException("Cannot be provided for 'GrouperGrm'")

}
