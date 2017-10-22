package anagram.ml.data

object CreateLearningDataGrammar {

  private val mapper = WordMapGrammar.createWordMapperFull

  val splitter: BookSplitter = new BookSplitterTxt()
  val screator: SentenceCreator = new SentenceCreatorSliding()
  val srater: SentenceRater = SentenceRaterGrammar
  val creator = new CreateLearningData(mapper, splitter, screator, srater)

  case class LinearAdjust(a: Double, k: Double)

  private lazy val adjustments = List(
    (2, LinearAdjust(365.1829,1453.9288)),
    (3, LinearAdjust(40.8586,159.4014)),
    (4, LinearAdjust(8.1286,23.6480)),
    (5, LinearAdjust(2.7777,4.7699)),
    (6, LinearAdjust(1.5649,1.6038)),
    (7, LinearAdjust(1.3092,1.1270)),
  ).toMap

  def adjustRating(rating: Double, sentLength: Int): Double = {
    val adj = adjustments(sentLength)
    (rating - adj.a) / adj.k
  }

  def createData(dataId: String, bookCollection: BookCollection): Unit = {
    val cfg = CreateDataConfig(
      dataId,
      bookCollection,
      2 to 7,
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