package anagram

object Tryout extends App {
  println(
    SentanceMorph
      .toRandom("Es war einmal und ist nicht mehr", 10)
      .map(_.mkString("   "))
      .mkString("\n")
  )

}