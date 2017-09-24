package anagram.ml.data

import java.io.BufferedWriter
import java.nio.file.{Path, Paths}

import anagram.common.IoUtil

object WordList {

  val impl = new WordListImpl

  def loadWordList(path: Path): Iterable[String] = {
    IoUtil.loadTxtFromPath(path, impl.loading)
  }

  def loadWordListFromWorkdir(id: String): Iterable[String] = {
    IoUtil.loadWordlistFromWorkDir(id, impl.loading)
  }

  def saveWordListToWorkdir(id: String, words: Iterable[String]): Path = {
    IoUtil.saveWordlistToWorkDir(id, impl.saving(words)(_))
  }

}

class WordListImpl {

  def loading(lines: Iterator[String]): Iterable[String] = {
    lines.toIterable
  }

  def saving(words: Iterable[String])(writer: BufferedWriter): Unit = {
    words.foreach(w => writer.write(s"$w\n"))
  }

}
