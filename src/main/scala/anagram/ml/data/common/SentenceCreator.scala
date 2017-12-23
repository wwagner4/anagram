package anagram.ml.data.common

sealed trait SentenceType

case object SentenceType_COMPLETE extends SentenceType
case object SentenceType_BEGINNING extends SentenceType
case object SentenceType_OTHER extends SentenceType
case object SentenceType_RANDOM extends SentenceType

case class Sentence (
                      sentenceType: SentenceType,
                      words: Seq[String]
)

trait SentenceCreator {

  /**
    * Creates typed sentences of a specific length from a sequence
    * of 'real' sentences
    */
  def create(sentences: Stream[Seq[String]], len: Int): Stream[Sentence]

}