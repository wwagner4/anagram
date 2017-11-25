package anagram.ml.data.analyze

import anagram.ml.rate.{Rater, RaterAi}
import anagram.model.Configurations
import anagram.words.Wordlists

object RandomRatedSentencesTryout extends App {

  val wlf = Wordlists.plainFreq100k

  val raterf = Configurations.plain.cfgRaterAi

  val rater: Rater = new RaterAi(raterf.cfgRaterAi())

  (2 to 5).foreach{len =>
    val  rf = raterf.description
    println(s"------- $len --- $rf")
    RandomSentences.create(10, len, wlf.wordList().toSeq).foreach { sent =>
      val r: Double = rater.rate(sent)
      println("%10.4f - %s".format(r, sent.mkString(" ")))
    }
  }

}
