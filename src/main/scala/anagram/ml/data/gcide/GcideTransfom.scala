package anagram.ml.data.gcide

import java.nio.file._

import scala.collection.JavaConverters._

object GcideTransfom extends App {

  val baseDir = Paths.get(System.getProperty("user.home"), "anagram", "gcide-0.51")
  require(Files.exists(baseDir))

  val ps1 = Files.newDirectoryStream(baseDir)
    .iterator()
    .asScala
    .toStream
    .filter(_.getFileName.toString.startsWith("CIDE"))


  ps1.foreach(p => println(p))

}
