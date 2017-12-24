package anagram.ml.data.analyze

import java.nio.file.Path

import anagram.common.IoUtil
import anagram.ml.rate.{Rater, RaterAi}
import anagram.model.{CfgRaterAi, Configurations, SentenceLength}
import anagram.words.{Word, WordListFactory, WordMapperRating, Wordlists}

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

    // ------------ CONFIGURATION -------------------
    val raterf = Configurations.plainRated.cfgRaterAi
    val wordLists = Seq(
      Wordlists.plainRatedLargeFine
    )
    // ------------ CONFIGURATION -------------------


    val n = 4000
    val doAdjust = false

    val cfgr: CfgRaterAi = copy(raterf.cfgRaterAi(), doAdjust)
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
      sents.flatMap { sent =>
        val r = rater.rate(sent)
        Some(OutRating(len, r))
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

    for ((len, maxRating) <- mr) {
      val diff = max - maxRating
      println(f"ratingAdjustOutput = $diff%.4f, // $len")
    }
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

  private def copy(cfg: CfgRaterAi, doAdjust: Boolean) = {
    if (cfg.adjustOutput == doAdjust) cfg
    else {
      new CfgRaterAi {

        override def mapper: WordMapperRating[_] = cfg.mapper

        override def adjustOutput: Boolean = doAdjust

        override def sentenceLengths: Iterable[SentenceLength] = cfg.sentenceLengths

        override def id: String = cfg.id
      }
    }
  }
}

object RandomSentences {

  private val ran = new Random()

  def createSentence(wl: Seq[Word], wlSize: Int, len: Int): Seq[String] = {
    (1 to len).map { _ =>
      val idx = ran.nextInt(wlSize)
      wl(idx).word
    }
  }

  def create(n: Int, len: Int, wl: Seq[Word]): Seq[Seq[String]] = {
    val wlSize = wl.size
    (1 to n) map { _ =>
      createSentence(wl, wlSize, len)
    }
  }
}