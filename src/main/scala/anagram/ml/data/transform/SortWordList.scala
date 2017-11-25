package anagram.ml.data.transform

import java.io.BufferedWriter
import java.net.URI
import java.nio.file.Paths

import anagram.common.IoUtil

object SortWordList extends App {

  val file: URI = IoUtil.uri("wordlist/wordlist_grammar_small.txt")

  val sorted: Iterable[(String, String)] = IoUtil.loadTxtFromFile(Paths.get(file), read)

  val filename = "wordlist_grammar_small.txt"

  IoUtil.save(IoUtil.dirWork, filename, (writer: BufferedWriter) => {
    sorted.foreach(value => {
      writer.write(s"${value._1};${value._2}\r")
    })
  })

  println(s"wrote sorted to workdir $filename")

  def read(iter: Iterator[String]): Iterable[(String, String)] = {
    val grouped = iter
      .map { line =>
        val split = line.split(";")
        require(split.size == 2, s"illegal line $split")
        (split(0), split(1))
      }
      .toSeq
      .groupBy(_._2)

    val distinct = grouped
      .toList
      .map { case (k: String, v: Iterable[(String, String)]) =>
        val typ = v
          .map(t => t._1)
          .distinct
          .mkString("&")
        (typ, k)
      }
    distinct
      .sortBy(_._2)
      .sortBy(_._2.length)
  }

}
