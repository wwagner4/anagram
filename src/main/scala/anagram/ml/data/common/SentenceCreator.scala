package anagram.ml.data.common

import anagram.words.WordMapper

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

  def create(sentences: Stream[Seq[String]], len: Int, wordMapper: WordMapper): Stream[Sentence]

}