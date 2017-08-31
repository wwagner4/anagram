package anagram

object Tryout extends App {

  val anas = List(
    Anagram("slot machines", "cash lost in em", 10),
    Anagram("clint eastwood", "old west action", 10),
    Anagram("william shakespeare", "ill make a wise phrase", 10),
    Anagram("election results", "lies lets recount", 10),
    Anagram("the best things in life are free", "nail biting refreshes the feet", 10),
    Anagram("rome was not built in a day", "any labour i do wants time", 10),
    Anagram("the meaning of life", "the fine game of nil", 10),
    Anagram("wir schaffen das", "warne das schiff", 10),
    Anagram("wagenspur golf", "super wolfgang", 10),
    Anagram("haus opern", "super noah", 10)
  )

  anas.foreach{ana =>
    println(
      SentanceMorph
        .morph(ana)
        .map(_.mkString(" "))
        .mkString("\n")
    )
    println()

  }


}
