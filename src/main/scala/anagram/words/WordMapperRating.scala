package anagram.words

case class MappingResult[T](
                             intermediate: T,
                             features: Seq[Double],
                           )

trait WordMapperRating[T] {

  def map(sentence: Seq[String]): MappingResult[T]

}
