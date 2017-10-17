package anagram.ml.data

import java.nio.file.Paths

import anagram.common.IoUtil

object WordList {

  def loadWordListSmall: Iterable[String] = loadWordList("wordlist/wordlist_small.txt")

  def loadWordListGrammer: Iterable[String] =
    loadWordList("wordlist/wordtypelist_full.txt")
      .map { line =>
        val s = line.split(";")
        require(s.length == 2)
        s(0)
      }
      .toSet
      .toSeq
      .sorted

  private def loadWordList(resName: String): Iterable[String] = {
    IoUtil.loadTxtFromPath(Paths.get(IoUtil.uri(resName)), (l) => l.toIterable)
  }

}
