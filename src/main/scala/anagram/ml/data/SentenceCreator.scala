package anagram.ml.data

import java.net.URI

sealed trait SentenceType

case object SentenceType_COMPLETE extends SentenceType
case object SentenceType_BEGINNING extends SentenceType
case object SentenceType_OTHER extends SentenceType

case class Sentence (
                      sentenceType: SentenceType,
                      words: Seq[String]
)

trait SentenceCreator {

  def create(books: Seq[URI], len: Int, wordMapper: WordMapper): Stream[Sentence]

}