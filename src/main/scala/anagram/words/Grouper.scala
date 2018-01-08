package anagram.words

trait Grouper {

  def group(value: String): Seq[String]

  def groups: Seq[String]

}
