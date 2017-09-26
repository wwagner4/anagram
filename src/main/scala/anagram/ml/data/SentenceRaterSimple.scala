package anagram.ml.data

import java.util.Locale

class SentenceRaterSimple(val wm: WordMapper, val variance: Int, val convertWordsToNumbers: Boolean) extends SentenceRater {

  val ran = new  util.Random()

  def rateSentence(sentence: Seq[String]): Seq[Seq[String]] = {

    val l = sentence.length
    val ratings = (0 to (100, 100 / l)).toList
    ratings.flatMap(rate => Seq.fill(5) {
      val numEx =  numExchange(sentence.size, rate)
      val sentEx: Seq[String] = exchange(sentence, numEx, wm)
      val sentNum = if (convertWordsToNumbers) sentEx.map(w => f(wm.toNum(w))) else sentEx
      val ranRate = rate + ran.nextInt(variance * 2 + 1) - variance
      sentNum :+ f(ranRate)
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

  def exchange(sent: Seq[String], numEx: Int,wm: WordMapper): Seq[String] = {
    val idx = ran.shuffle(sent.indices.toList).take(numEx)
    for ((w, i) <- sent.zipWithIndex) yield {
      if (idx.contains(i)) wm.randomWord
      else w
    }
  }

}
