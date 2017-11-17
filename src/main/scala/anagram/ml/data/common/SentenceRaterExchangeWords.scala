package anagram.ml.data.common

import java.util.Locale

import anagram.words.WordMapper

/**
  * Rates every existing sentence with 100. Then creates new sentences
  * from the existing sentences by exchanging one ore more words
  * by random words. These created sentences are rated lower than 100
  * depending on the amount of words exchanged
  */
class SentenceRaterExchangeWords(val wm: WordMapper) extends SentenceRater {

  val ran = new  util.Random()

  def rateSentence(sentences: Iterable[Sentence]): Iterable[Rated] = {

    sentences.flatMap { sentence =>
      val l = sentence.words.length
      val ratings = (0 to (100, 100 / l)).toList
      ratings.flatMap(rate => Seq.fill(5) {
        val numEx =  numExchange(sentence.words.size, rate)
        val sentEx = exchange(sentence, numEx)
        Rated(sentEx, rate)
      })
    }
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

  def exchange(sent: Sentence, numEx: Int): Sentence = {
    val idx = ran.shuffle(sent.words.indices.toList).take(numEx)
    val wordsExc = for ((w, i) <- sent.words.zipWithIndex) yield {
      if (idx.contains(i)) wm.randomWord
      else w
    }
    Sentence(sent.sentenceType, wordsExc)
  }

}
