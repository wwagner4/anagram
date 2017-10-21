package anagram.ml.data

import java.nio.file.Paths

import anagram.common.IoUtil

case class Word(word: String, wordSorted: String)

object WordList {

  def loadWordListSmall: Iterable[Word] = loadWordList("wordlist/wordlist_small.txt")
    .map(line => Word(line, line.sorted))

  def loadWordListGrammar: Iterable[Word] =
    loadWordList("wordlist/wordtypelist_full.txt")
      .map { line =>
        val s = line.split(";")
        require(s.length == 2)
        s(0)
      }
      .toSet
      .toSeq
      .sorted
      .map(w => Word(w, w.sorted))

  private def loadWordList(resName: String): Iterable[String] = {
    IoUtil.loadTxtFromPath(Paths.get(IoUtil.uri(resName)), (l) => l.toIterable)
  }

}
