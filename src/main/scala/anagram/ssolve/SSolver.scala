package anagram.ssolve

object SSolver {

  def solve(sourceText: String, words: Iterable[String]): Iterable[Iterable[String]] = {
    solve(sourceText, 0, words.toList)
  }

  def solve(txt: String, depth: Int, words: List[String]): List[List[String]] = {
    if (txt.isEmpty) List.empty[List[String]]
    else {
      val mws = findMatchingWords(txt, words)
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

  def validWord(w: String, txt: String): Option[String] = {
    def validWord1(w: String, wl: List[Char], txt: String): Option[String] = {
      wl match {
        case Nil => Some(w)
        case c :: rest =>
          if (txt.indexOf(c) >= 0) validWord1(w, rest, txt.replaceFirst(s"$c", ""))
          else None
      }
    }

    validWord1(w, w.toList, txt)
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
