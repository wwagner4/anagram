package anagram.ml.data

import java.nio.file.{Path, Paths}

import anagram.common.IoUtil

object WordList {

  val impl = new WordListImpl

  def loadWordList(path: Path): Iterable[String] = {
    IoUtil.loadTxtFromPath(path, impl.loading)
  }

  def defaultWordlist: Path = {
    val resName = "wordlist/wordlist.txt"
    val url = getClass.getClassLoader.getResource(resName)
    require(url != null, s"Classpath resourse '$resName' does not exist")
    Paths.get(url.toURI)
  }

}

class WordListImpl {

  def loading(lines: Iterator[String]): Iterable[String] = {
    lines.toIterable
  }

}
