package anagram.ml.data.analyze

import anagram.words.{Word, Wordlists}

object WordTypesAnalyze extends App {

  val words: Seq[Word] = Wordlists.grammar.wordList().toSeq
  val words2 = Set("a", "i")

  val inter = words.filter(gw => words2.contains(gw.word)).toList
  println(inter)

  def readLine(line: String): Word = {
    val split = line.split(";")
    Word(split(0), split(0).sorted, Some(split(1)))
  }

  val popGrps = Set(
    "n",
    "a",
    "vt",
    "adv",
    "adj",
    "npl",
    "vi",
    "propn",
    "pron",
    "prep",
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

      if (!popGrps.contains(g1)) "?"
      else g2
    }
  }


  val words1: Seq[Word] = words.map(gw => gw.copy(grp = Some(reduceGroups(gw.grp.get))))

  val trip: Seq[(Int, String, String)] = for ((a, b) <- words1.groupBy(_.grp.get).toSeq) yield {
    val size = b.size
    val l = b.take(20).map(_.word).mkString(",")
    (size, a, l)
  }

  for ((s, a, l) <- trip.sortBy(-_._1)) {
    println("%10d %30s %s".format(s, a, l))
  }


}
