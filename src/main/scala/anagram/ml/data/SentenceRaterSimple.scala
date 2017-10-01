package anagram.ml.data

import java.util.Locale

class SentenceRaterSimple(val wm: WordMapper) extends SentenceRater {

  val ran = new  util.Random()

  def rateSentence(sentence: Sentence): Seq[Rated] = {

    val l = sentence.words.length
    val ratings = (0 to (100, 100 / l)).toList
    ratings.flatMap(rate => Seq.fill(5) {
      val numEx =  numExchange(sentence.words.size, rate)
      val sentEx = exchange(sentence, numEx)
      Rated(sentEx, rate)
    })
  }

  def numExchange(sentSize: Int, rating: Int): Int = {
    require(sentSize >= 2)
    require(rating >= 0)
    require(rating <= 100)
    val re = (((100 - rating).toDouble / 100) * sentSize).toInt
    if (sentSize == 2 && rating < 100 && rating > 50) re + 1
    else if (sentSize == 3 && rating < 100 && rating > 30) re + 1
    else re
  }

  def f(value: Int): String = "%d".formatLocal(Locale.ENGLISH, value)

  // TODO New Sentence type
  def exchange(sent: Sentence, numEx: Int): Sentence = {
    val idx = ran.shuffle(sent.words.indices.toList).take(numEx)
    val wordsExc = for ((w, i) <- sent.words.zipWithIndex) yield {
      if (idx.contains(i)) wm.randomWord
      else w
    }
    Sentence(sent.sentenceType, wordsExc)
  }

}
