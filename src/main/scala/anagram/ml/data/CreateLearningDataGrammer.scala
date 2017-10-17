package anagram.ml.data

import java.net.URI
import java.nio.file.Paths

import anagram.common.IoUtil

object CreateLearningDataGrammer {

  val wordList = WordList.loadWordListGrammer
  val mapper = WordMapSingleWord.createWordMapperFromWordlist(wordList)
  val grouper = WordGrouperGrammer

  val splitter = new BookSplitterTxt()
  val screator = new SentenceCreatorSliding()
  val srater = new SentenceRaterStraight(mapper)

  val creator = new CreateLearningData(mapper, grouper, splitter, screator, srater)

  def createData(dataId: String, bookCollection: BookCollection): Unit = {
    creator.createData(dataId, bookCollection)
  }

}

object WordGrouperGrammer extends WordGrouper {

  private val uri: URI = IoUtil.uri("wordlist/wordtypelist_full.txt")

  val w2g: Map[String, String] = IoUtil.loadTxtFromPath(Paths.get(uri), iter => {
    iter
      .toIterable
      .map { line =>
        val split = line.split(";")
        require(split.length == 2, s"could not split '$line' by ';'")
        (split(1), split(0))
      }
      .toMap
  })

  override def group(word: String): String = {
    w2g.get(word).getOrElse("???")
  }

}
