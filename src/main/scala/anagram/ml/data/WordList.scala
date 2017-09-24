package anagram.ml.data

import java.io.BufferedWriter
import java.nio.file.Paths

import anagram.common.IoUtil

object WordList {

  val impl = new WordListImpl

  def loadWordList(resName: String): Iterable[String] = {
    IoUtil.loadTxtFromPath(Paths.get(IoUtil.uri(resName)), impl.loading)
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
