package anagram.model.grmred

import anagram.words.{Grouper, Word}

class GrouperGrmRed(wl: Iterable[Word]) extends Grouper {

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

  private lazy val _grps: Seq[String] = (popularGrps + unknown).toSeq

  private lazy val words: Seq[Word] = wl.toSeq.map(w => w.copy(grp = Some(reduceGroups(w.grp.get))))

  private lazy val wordMap: Map[String, Word] = words.map(gword => (gword.word, gword)).toMap

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

  override def group(value: String): Seq[String] =
    Seq(wordMap.get(value).map(_.grp.get).getOrElse(unknown))

  override def groups: Seq[String] = _grps
}
