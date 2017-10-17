package anagram.ml.data

import anagram.common.IoUtil

object SentenceCreatorTryout extends App {

  val uris = BookSplitterTxt.booksBig.toStream.map(IoUtil.uri)
  val wordList = IoUtil.loadWordList("wordlist/wordlist_small.txt")
  val wm = WordMapSingleWord.createWordMapperFromWordlist(wordList)
  val wg = WordGrouperIdentity
  val splitter = new BookSplitterTxt
  val creator = new SentenceCreatorSliding()

  showSentences
  //completeWords

  def completeWords: Unit = {
    val split = uris.flatMap(splitter.splitSentences)

    List(2, 3, 4, 5, 6, 7).map { size =>
      val stat: Map[SentenceType, Stream[Sentence]] = creator.create(split, size, wm, wg)
        .groupBy(s => s.sentenceType)

      stat.foreach{ case (k, v) =>
        println("%3d - %4s - %d" format(size, short(k), v.size))
      }

    }
  }

  def showSentences: Unit = {
    val split = uris.flatMap(splitter.splitSentences)
    val sent = creator.create(split, 2, wm, wg)
    sent.foreach { sent =>
      val ws = sent.words.mkString(" ")
      println("%5s - %s" format(short(sent.sentenceType), ws))
    }
  }

  private def short(sentenceType: SentenceType): String = {
    sentenceType match {
      case SentenceType_BEGINNING => "B"
      case SentenceType_COMPLETE => "C"
      case SentenceType_OTHER => "O"
    }
  }
}

