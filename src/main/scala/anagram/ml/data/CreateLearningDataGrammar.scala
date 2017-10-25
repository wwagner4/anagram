package anagram.ml.data

object CreateLearningDataGrammar {

  private val mapper = WordMapGrammar.createWordMapperFull

  val splitter: BookSplitter = new BookSplitterTxt()
  val screator: SentenceCreator = new SentenceCreatorSliding()
  val srater: SentenceRater = SentenceRaterGrammar
  val creator = new CreateLearningData(mapper, splitter, screator, srater)

  case class LinearAdjust(a: Double, k: Double)

  private lazy val adjustments = List(
    (2, LinearAdjust(31.2029,229.6509)),
    (3, LinearAdjust(4.8286,23.7397)),
    (4, LinearAdjust(1.9724,4.4061)),
    (5, LinearAdjust(1.4014,1.5247)),
  ).toMap

  def adjustRating(rating: Double, sentLength: Int): Double = {
    val adj = adjustments(sentLength)
    (rating - adj.a) / adj.k
  }

  def createData(dataId: String, bookCollection: BookCollection): Unit = {
    val cfg = CreateDataConfig(
      dataId,
      bookCollection,
      2 to 5,
      adjustRating,
    )
    creator.createData(cfg)
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