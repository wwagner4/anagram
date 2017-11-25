package anagram.ml.data.analyze

import java.nio.file.Path

import anagram.common.IoUtil
import anagram.ml.rate.{Rater, RaterAi}
import anagram.model.{CfgRaterAiImpl, CfgRaterAiImplCreator, Configurations}
import anagram.words.{Word, WordListFactory, Wordlists}

import scala.util.Random

case class OutRating(sentLen: Int, rating: Double)

/**
  * The NNs for different Length produce output values
  * in different ranges. This program calculates a factor for
  * each sentence length, in order to make the output values
  * for sentences with different lengths comparable.
  */
object SentenceLengthRatingDiff extends App {

  case class Result(
                     desc: String,
                     maxRatings: Seq[(Int, Double)]
                   )


  maxRatingsFromRandomSentences()

  def maxRatingsFromRandomSentences(): Unit = {

    val raterf = Configurations.grammarReduced.cfgRaterAi

    val wordLists = Seq(
      Wordlists.plainFreq2k,
      Wordlists.plainFreq3k,
      Wordlists.plainFreq5k,
      Wordlists.plainFreq50k,
      Wordlists.plainFreq100k,
    )
    val n = 2000
    val doAdjust = false

    val cfgr = CfgRaterAiImplCreator.create(raterf.cfgRaterAi()).copy(adjustOutput = doAdjust)
    val rater = new RaterAi(cfgr)

    def result: Result = {
      val outRatings: Seq[OutRating] = wordLists
        .flatMap(wlf => (2 to 5)
          .flatMap(l => outRatingsRandom(l, wlf, rater)))
      val _mr = maxRatings(outRatings)
      val _da = if (doAdjust) "adjust" else "NO adjust"
      val _desc = s"--- ${raterf.description} --- ALL --- ${_da}"
      Result(_desc, _mr)
    }

    def outRatingsRandom(len: Int, wlf: WordListFactory, rater: Rater): Seq[OutRating] = {
      val wl = wlf.wordList().toSeq
      val sents = RandomSentences.create(n, len, wl)
      sents.map { sent =>
        val r = rater.rate(sent)
        OutRating(len, r)
      }
    }

    val r = result
    println()
    println(r.desc)
    println()
    output(r.maxRatings)
    println()
    output1(r.maxRatings)

  }


  def maxRatingsFromAnagrams(): Unit = {
    def anagramfileNames: Seq[Path] = {
      val iter = for (
        dir <- IoUtil.allSubdirs(IoUtil.dirAnagrams.resolve("04"));
        file <- IoUtil.allFiles(dir)
      ) yield file
      iter.toSeq
    }

    val mr = maxRatings(anagramfileNames.flatMap(read))
    println("--- max rating from anagrams ---")
    output(mr)
  }

  def output(mr: Seq[(Int, Double)]): Unit = {
    val max = mr.map(t => t._2).max

    val h1 = "length"
    val h2 = "max"
    val h3 = "diff"
    println(f"$h1%10s $h2%6s $h3%6s")
    for ((len, maxRating) <- mr) {
      val diff = max - maxRating
      println(f"$len%10d $maxRating%6.4f $diff%6.4f")
    }
  }

  def output1(mr: Seq[(Int, Double)]): Unit = {
    val max = mr.map(t => t._2).max

    println(f"len match {")
    println(f"case 1 => rating + 20")
    for ((len, maxRating) <- mr) {
      val diff = max - maxRating
      println(f"case $len => rating + $diff%.4f")
    }
    println(f"case _ => rating - 20")
    println(f"}")
  }


  def read(file: Path): Iterator[OutRating] = {
    IoUtil.loadTxtFromFile(file, (iter) => iter.map(s => mkGenRating(s)))
  }


  def maxRatings(ratings: Seq[OutRating]): Seq[(Int, Double)] = ratings
    .groupBy(x => x.sentLen)
    .filter(t => t._1 >= 2)
    .filter(t => t._1 <= 5)
    .toList
    .map { case (k, v) => (k, v.map(_.rating).max) }
    .sortBy { case (k, _) => k }

  // example line
  //   10 - 0.20908 - 'cd hi i fit lows'

  def mkGenRating(s: String): OutRating = {
    val idx1 = s.indexOf('-', 0)
    require(idx1 >= 0)
    val idx2 = s.indexOf("- ", idx1 + 1)
    require(idx2 >= 0)
    val idx3 = s.indexOf('\'', 0)
    require(idx3 >= 0)
    val idx4 = s.indexOf('\'', idx3 + 1)
    require(idx4 >= 0)
    val ratingStr = s.substring(idx1 + 2, idx2 - 1)
    val sent = s.substring(idx3, idx4)

    //println(s"'$s' + ($idx1 $idx2) ($idx3 $idx4) :: '$ratingStr' :: '$sent'")

    if (sent.isEmpty || ratingStr.isEmpty) OutRating(0, 0.0)
    else OutRating(sent.split("\\s").length, ratingStr.toDouble)
  }


}

object RandomSentences {

  private val ran = new Random()

  def createSentence(wl: Seq[Word], wlSize: Int, len: Int): Iterable[String] = {
    (1 to len).map { _ =>
      val idx = ran.nextInt(wlSize)
      wl(idx).word
    }
  }

  def create(n: Int, len: Int, wl: Seq[Word]): Seq[Iterable[String]] = {
    val wlSize = wl.size
    (1 to n) map { _ =>
      createSentence(wl, wlSize, len)
    }
  }
}