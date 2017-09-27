package anagram.ml.data

class SentenceRaterAdapted(val wm: WordMapper) extends SentenceRater {

  val ran = new util.Random()


  def rateSentence(sentence: Seq[String]): Seq[Rated] = {

    def randomExchange(idx: Seq[Int], rating: Int): Rated = {
      val exchaned = for ((w, i) <- sentence.zipWithIndex) yield {
        if (idx.contains(i)) wm.randomWord
        else w
      }
      Rated(exchaned, rating)
    }

    def randomExchangeN(n: Int, rating: Int): Rated = {
      val idx = ran.shuffle(sentence.indices.toList).take(n)
      val exchaned = for ((w, i) <- sentence.zipWithIndex) yield {
        if (idx.contains(i)) wm.randomWord
        else w
      }
      Rated(exchaned, rating)
    }

    def ratedIdentity(rate: Double): Rated = Rated(sentence, rate)

    def ratedRandom(rate: Double): Rated = {
      val sent = sentence.indices.map(_ => wm.randomWord)
      Rated(sent, rate)
    }

    sentence.length match {
      case 1 => Seq(
        ratedIdentity(100),
        ratedRandom(0),
      )
      case 2 => Seq(
        ratedIdentity(100),
        ratedRandom(0),
      )
      case 3 => Seq(
        ratedIdentity(100),
        randomExchange(Seq(0), 33),
        randomExchange(Seq(1), 33),
        randomExchange(Seq(2), 33),
        ratedRandom(0),
      )
      case 4 => Seq(
        ratedIdentity(100),
        randomExchangeN(1, 66),
        randomExchangeN(1, 66),
        randomExchangeN(1, 66),
        randomExchangeN(2, 33),
        randomExchangeN(2, 33),
        randomExchangeN(2, 33),
        ratedRandom(0),
      )
      case 5 => Seq(
        ratedIdentity(100),
        randomExchangeN(1, 70),
        randomExchangeN(1, 70),
        randomExchangeN(1, 70),
        randomExchangeN(1, 70),
        randomExchangeN(2, 40),
        randomExchangeN(2, 40),
        randomExchangeN(2, 40),
        randomExchangeN(2, 40),
        ratedRandom(0),
      )
      case 6 => Seq(
        ratedIdentity(100),
        randomExchangeN(1, 75),
        randomExchangeN(1, 75),
        randomExchangeN(1, 75),
        randomExchangeN(2, 50),
        randomExchangeN(2, 50),
        randomExchangeN(2, 50),
        randomExchangeN(3, 25),
        randomExchangeN(3, 25),
        randomExchangeN(3, 25),
        ratedRandom(0),
      )
      case 7 => Seq(
        ratedIdentity(100),
        randomExchangeN(1, 80),
        randomExchangeN(1, 80),
        randomExchangeN(1, 80),
        randomExchangeN(2, 60),
        randomExchangeN(2, 60),
        randomExchangeN(2, 60),
        randomExchangeN(3, 30),
        randomExchangeN(3, 30),
        randomExchangeN(3, 30),
        ratedRandom(0),
      )
      case _ => throw new IllegalStateException(s"Cannot rate sentence with ${sentence.size} words")
    }
  }


}
