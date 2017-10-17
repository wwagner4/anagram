package anagram.ml.data

class SentenceRaterAdapted(val wm: WordMapper) extends SentenceRater {

  val ran = new util.Random()


  def rateSentence(sentence: Sentence): Seq[Rated] = {

    def randomExchange(idx: Seq[Int], rating: Int): Rated = {
      val exchaned = for ((w, i) <- sentence.words.zipWithIndex) yield {
        if (idx.contains(i)) wm.randomWord
        else w
      }
      Rated(Sentence(sentence.sentenceType, exchaned), rating)
    }

    def randomExchangeN(n: Int, rating: Int): Rated = {
      val idx = ran.shuffle(sentence.words.indices.toList).take(n)
      val exchaned = for ((w, i) <- sentence.words.zipWithIndex) yield {
        if (idx.contains(i)) wm.randomWord
        else w
      }
      Rated(Sentence(sentence.sentenceType, exchaned), rating)
    }


    val preferredFirstWords = Seq("i", "you", "he", "she", "it",
      "a", "an", "the", "my", "your", "her", "their",
      "red", "green", "yellow", "blue", "white", "black")

    def ratedIdentityPreferredWords(preferredRating: Double, identityRating: Double): Rated = {
      require(sentence.words.nonEmpty)
      val  rating = if (preferredFirstWords.contains(sentence.words(0))) 100
      else 80
      Rated(sentence, rating)
    }

    def ratedIdentity(rate: Double): Rated = Rated(sentence, rate)

    def ratedRandom(rate: Double): Rated = {
      val sent = sentence.words.indices.map(_ => wm.randomWord)
      Rated(Sentence(sentence.sentenceType, sent), rate)
    }

    val rated: Seq[Rated] = sentence.words.length match {
      case 1 => Seq(
        ratedIdentity(100),
        ratedRandom(0),
      )
      case 2 => Seq(
        ratedIdentityPreferredWords(100, 80),
        ratedRandom(0),
      )
      case 3 => Seq(
        ratedIdentityPreferredWords(100, 80),
        randomExchange(Seq(0), 33),
        randomExchange(Seq(1), 33),
        randomExchange(Seq(2), 33),
        ratedRandom(0),
      )
      case 4 => Seq(
        ratedIdentity(100),
        ratedIdentity(100),
        ratedIdentity(100),
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
        ratedIdentity(100),
        ratedIdentity(100),
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
        ratedIdentity(100),
        ratedIdentity(100),
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
        ratedIdentity(100),
        ratedIdentity(100),
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
      case _ => throw new IllegalStateException(s"Cannot rate groups with ${sentence.words.size} groups")
    }

    rated.map(r => r.sentence.sentenceType match {
      case SentenceType_OTHER => r
      case SentenceType_COMPLETE => Rated(r.sentence, r.rating * 1.5)
      case SentenceType_BEGINNING => Rated(r.sentence, r.rating * 1.2)
    })
  }


}
