package anagram.solve

import anagram.common.{IoUtil, SortedList}
import anagram.words.{WordMapper, WordMappers}
import org.slf4j.LoggerFactory

case class CfgAiSolver(
                        id: String,
                        mapper: WordMapper,
                      )

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
    "ingrid bernd in love", //                          17 ->
    "william shakespeare", // -> i am a weakish speller 18 ->
  )

  val srcTextsMedium = List(
    "wolfgang",
    "ditschi",
    "ingrid bernd",
    "ditschi wolfi", //                                 12 -> 17k
    "noah the great", //                                12 -> 708k
    "clint eastwood", // -> old west action             13 -> 700k
  )

  val srcTextsShort = List(
    "wolfgang",
    "ditschi",
    "ingrid bernd",
  )

  val srcTextsWs = List(
    "william shakespeare", // -> i am a weakish speller 18 ->
  )
  val cfgPlain = CfgAiSolver("enPlain11", WordMappers.createWordMapperPlain)
  val cfgGrm = CfgAiSolver("enGrm11", WordMappers.createWordMapperGrammer)

  val idSolving: String = "01"
  val srcTexts = srcTextsMedium
  val cfg = cfgPlain

  val ignoreWords = Seq(
    "ere",
    "nth",
    "id",
    "dreg",
    "cal",
    "inc",
    "nevi",
    "von",
    "cit",
    "esc",
    "alt",
    "brin",
    "veer",
    "brin",
    "bin",
    "nil",
    "chi",
    "cd",
    "ohs",
    "lith",
    "noir",
    "veda",
    "vade",
    "vinal",
    "dict",
    "wonts",
    "wots",
    "odic",
    "orth",
    "dows",
    "thor",
    "ghee",
    "attn",
    "din",
    "led",
  ).toSet
  val wordlist = WordMappers.createWordMapperPlain
    .wordList
    .filter(w => !ignoreWords.contains(w.word))


  for (srcText <- srcTexts) {
    log.info(s"Solving $srcText")

    val rater: Rater = new RaterAi(cfg.id, cfg.mapper, None)
    //val rater: Rater = new RaterNone
    val baseSolver = SolverImpl(maxDepth = 4, parallel = 5)
    val aiSolver = SolverRating(baseSolver, rater)

    val anagrams: Stream[Ana] = aiSolver.solve(srcText, wordlist)
    //writeToFile(anagrams, srcText)
    //stdout(anagrams, srcText)
    iter(anagrams, srcText)
  }
  log.info("Finished")

  def iter(anas: Stream[Ana], srcText: String): Unit = {
    val solverIter = SolverIter.instance(anas, 10)
    while (solverIter.hasNext) {
      for (anas <- solverIter.toStream) {
        if (anas.isEmpty) {
          log.info("-- NO RESULT SO LONG --")
        } else {
          val re = anas
            .map(ana => ana.sentence.mkString(" "))
            .map(anaStr => "%20s".format(anaStr))
            .mkString(", ")
          log.info(s"$srcText -> $re")
        }
        Thread.sleep(1000)
      }
    }
  }

  def stdout(anas: Stream[Ana], srcText: String): Unit = {

    log.info(s"searching anagrams for: '$srcText'")

    val sl = SortedList.instance(new OrderingAnaRatingDesc)
    anas.foreach(ana => sl.add(ana))

    val re: String = sl.take(10)
      .map(ana => ana.sentence.mkString(" "))
      .map(anaStr => "%20s".format(anaStr))
      .mkString(", ")
    log.info(s"found: $re")

  }

  def writeToFile(anas: Stream[Ana], srcText: String): Unit = {

    def fileName(idLearning: String, idSolving: String, src: String): String = {
      val s1 = src.replaceAll("\\s", "_")
      s"anagrams_${idLearning}_${idSolving}_$s1.txt"
    }

    val fn = fileName(cfg.id, idSolving, srcText)
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



