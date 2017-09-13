package anagram.ml.data

import java.io.BufferedWriter
import java.nio.file.{Files, Path, Paths}

object IoUtil {

  def getCreateWorkDir: Path = {
    val dirWork: Path = Paths.get(System.getProperty("user.home"), "anagram", "work")
    if (!Files.exists(dirWork)) {
      Files.createDirectories(dirWork)
    }
    dirWork
  }

  def save(dir: Path, fileName: String, f: BufferedWriter => Unit): Unit = {
    val file = dir.resolve(fileName)
    val wr: BufferedWriter = Files.newBufferedWriter(file)
    try f(wr) finally wr.close()
  }

  def saveTxtToWorkDir(id: String, f: BufferedWriter => Unit): Unit = {
    val filename = s"anagram_$id.txt"
    save(getCreateWorkDir, filename, f)
  }

}
