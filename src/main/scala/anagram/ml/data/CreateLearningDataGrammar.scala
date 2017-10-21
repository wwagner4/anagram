package anagram.ml.data

object CreateLearningDataGrammar {

  private val mapper = WordMapGrammar.createWordMapperFull

  val splitter: BookSplitter = new BookSplitterTxt()
  val screator: SentenceCreator = new SentenceCreatorSliding()
  val srater: SentenceRater = SentenceRaterGrammar
  val creator = new CreateLearningData(mapper, splitter, screator, srater, mapWordsToNumbers = false)

  def createData(dataId: String, bookCollection: BookCollection): Unit = {
    creator.createData(dataId, bookCollection)
  }

}

object SentenceRaterGrammar extends SentenceRater {
  override def rateSentence(sentences: Iterable[Sentence]): Iterable[Rated] = {
    val rmap: Seq[(Seq[String], Iterable[Sentence])] = sentences.groupBy(sent => sent.words).toSeq
    for ((w, sentences) <- rmap) yield {
      val y: Seq[(SentenceType, Int)] = sentences.map(_.sentenceType).groupBy(identity).mapValues(_.size).toSeq
      val rating: Double = y.foldLeft(0.0) {
        case (r, (stype, cnt)) => stype match {
          case SentenceType_COMPLETE => r + cnt * 10
          case SentenceType_BEGINNING => r + cnt * 5
          case SentenceType_OTHER => r + cnt * 1
        }
      }
      Rated(Sentence(SentenceType_OTHER, w), rating)
    }
  }
}