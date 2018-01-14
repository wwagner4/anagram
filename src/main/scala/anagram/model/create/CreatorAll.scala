package anagram.model.create

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import anagram.ml.DataCollectorViz

object CreatorAll extends AbstractCreator with App {

  val ts = timestamp

  private val dc: DataCollectorViz = DataCollectorViz(s"anaAllModels$ts", s"Anagram all models $ts")
  all(dc)
  dc.output()


  def timestamp: String = {
    val dt = LocalDateTime.now()
    val f = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    f.format(dt)
  }

}

