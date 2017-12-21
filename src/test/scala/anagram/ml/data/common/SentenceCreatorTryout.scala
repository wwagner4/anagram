package anagram.ml.data.common

import anagram.model.plain.WordMapperFactoryPlain
import anagram.words.Wordlists

object SentenceCreatorTryout extends App {

  val resNames = BookSplitterTxt.booksBig.toStream
  private val wl = Wordlists.plain.wordList()
  val wm = new WordMapperFactoryPlain(wl).create
  val splitter = new BookSplitterTxt
  val creator = new SentenceCreatorSliding(wm)

  showSentences()
  //completeWords

  def completeWords(): Unit = {
    val split = resNames.flatMap(splitter.splitSentences)

    List(2, 3, 4, 5, 6, 7).foreach { size =>
      val stat: Map[SentenceType, Stream[Sentence]] = creator.create(split, size)
        .groupBy(s => s.sentenceType)

      stat.foreach{ case (k, v) =>
        println("%3d - %4s - %d" format(size, short(k), v.size))
      }

    }
  }

  def showSentences(): Unit = {
    val split = resNames.flatMap(splitter.splitSentences)
    val sent = creator.create(split, 2)
    sent.foreach { sent =>
      val ws = sent.words.mkString(" ")
      println("%5s - %s".format(short(sent.sentenceType), ws))
    }
  }

  private def short(sentenceType: SentenceType): String = {
    sentenceType match {
      case SentenceType_BEGINNING => "B"
      case SentenceType_COMPLETE => "C"
      case SentenceType_OTHER => "O"
      case SentenceType_RANDOM => "R"
    }
  }
}

