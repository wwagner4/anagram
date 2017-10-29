package anagram.solve

import anagram.common.{IoUtil, SortedList}
import anagram.words.WordMappers
import org.slf4j.LoggerFactory

object AiSolverMain extends App {

  val log = LoggerFactory.getLogger("anagram.solve.AiSolverMain")

  val srcTextsFull = List(
    "wolfgang",
    "ditschi",
    "ingrid bernd",
    "ditschi wolfi", //                                 12 -> 17k
    "noah the great", //                                12 -> 708k
    "clint eastwood", // -> old west action             13 -> 700k
    "leornado da vinci", // -> did color in a nave      15 -> 1900k
    "william shakespeare", // -> i am a weakish speller 18 ->
    "ingrid bernd in love", //                          17 ->
  )

  val srcTextsShort = List(
    "wolfgang",
    "ditschi",
    "ingrid bernd",
  )

  val srcTexts = srcTextsShort

  val idLearning: String = "enGrm11"
  val idSolving: String = "01"

  val wordMapper = WordMappers.createWordMapperGrammer

  val ignoreWords = Seq(
    "ere",
    "nth",
    "id",
    "dreg",
  ).toSet
  val wordlist = WordMappers.createWordMapperPlain
    .wordList
    .filter(w => !ignoreWords.contains(w.word))

  val rater: Rater = new RaterAi(idLearning, wordMapper)
  //val rater: Rater = new RaterNone
  val baseSolver = SSolver(maxDepth = 4, parallel = 5)
  val aiSolver = AiSolver(baseSolver, rater)

  for (srcText <- srcTexts) {
    val anas: Stream[Ana] = aiSolver.solve(srcText, wordlist)
    //writeToFile(anas, srcText)
    stdout(anas, srcText)
  }
  log.info("Finished")


  def stdout(anas: Stream[Ana], srcText: String): Unit = {

    log.info(s"searching anagrams for: '$srcText'")

    val sl = SortedList.instance(new OrderingAnaRatingDesc)
    anas.foreach(ana => sl.add(ana))

    val re: String = sl.take(10).map(ana => ana.sentence.mkString(" ")).mkString(", ")
    log.info(s"found: $re")
  }

  def writeToFile(anas: Stream[Ana], srcText: String): Unit = {

    def fileName(idLearning: String, idSolving: String, src: String): String = {
      val s1 = src.replaceAll("\\s", "_")
      s"anagrams_${idLearning}_${idSolving}_$s1.txt"
    }

    val fn = fileName(idLearning, idSolving, srcText)
    log.info(s"Write anagrams for '$srcText' to $fn")
    IoUtil.saveToWorkDir(fn, (bw) => {
      var cnt = 0
      for ((ana, i) <- anas.toList.sortBy(-_.rate).zipWithIndex) {
        if (cnt % 10000 == 0 && cnt > 0) log.info(s"Wrote $cnt anagrams")
        bw.append("%5d - %5.5f - '%s'%n".format(i + 1, ana.rate, ana.sentence.mkString(" ")))
        cnt += 1
      }
    })
  }
}

class OrderingAnaRatingDesc extends Ordering[Ana] {
  override def compare(x: Ana, y: Ana): Int = y.rate.compareTo(x.rate)
}