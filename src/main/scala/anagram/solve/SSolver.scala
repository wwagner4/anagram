package anagram.solve

object SSolver {

  def solve(sourceText: String, words: Iterable[String]): Seq[Iterable[String]] = {
    solve(sourceText.toLowerCase().replaceAll("\\s", ""), 0, words.toList)
  }

  def solve(txt: String, depth: Int, words: List[String]): List[List[String]] = {
    val re = if (txt.isEmpty) List.empty[List[String]]
    else {
      if (depth > 4) List.empty[List[String]]
      else {
        val mws = findMatchingWords(txt, words)
          .filter(!_.isEmpty)
        //if (depth <= 2) {
        //  val t = ":" + txt + ":"
        //  println(f"-- found matching words: $depth $t%12s ${mws.take(30).mkString(" ")}")
        //}
        mws.flatMap { mw =>
          val restText = removeChars(txt, mw.toList)
          val subAnas = solve(restText, depth + 1, words)
          if (restText.isEmpty && subAnas.isEmpty) {
            List(List(mw))
          } else {
            subAnas.map(sent => mw :: sent)
          }
        }
      }
    }
    val t = ":" + txt + ":"
    println(f"-- found anagrams: $depth $t%12s ${re.take(30).mkString(" ")}")
    re
  }

  def validWord(word: String, txt: String): Option[String] = {
    def vw(w: String, txt: String): Option[String] = {
      if (w.isEmpty) Some(word)
      else {
        val l = w.length
        val head = w.substring(0, 1)(0)
        val tail = w.substring(1, l)
        val i = txt.indexOf(head)
        if (i >= 0) vw(tail, replaceFirst(head, txt, i))
        else None
      }
    }
    vw(word, txt)
  }

  def replaceFirst(c: Char, s: String, i: Int): String = {
    val l = s.length
    if (i == 0) {
      if (l == 1) ""
      else s.substring(1, l)
    }
    else {
      if (l == 1) ""
      else s.substring(0, i) + s.substring(i + 1, l)
    }
  }

  def findMatchingWords(txt: String, words: List[String]): List[String] = {
    words.flatMap(w => validWord(w, txt))
  }

  def removeChars(txt: String, mw: List[Char]): String = {
    mw match {
      case Nil => txt
      case c :: rest =>
        val txt1 = txt.replaceFirst(s"$c", "")
        removeChars(txt1, rest)
    }
  }

}
