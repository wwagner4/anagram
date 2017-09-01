package anagram

import java.io.File

object Tryout extends App {

  case class Anagram(
                      name: String,
                      from: String,
                      to: String,
                      lines: Int
                    )

  val anas = List(
    Anagram("slotmachine", "slot machines", "cash lost in em", 8),
    Anagram("clint", "clint eastwood", "old west action", 8),
    Anagram("william", "william shakespeare", "ill make a wise phrase", 6),
    Anagram("lies", "election results", "lies lets recount", 6),
    Anagram("bestthings", "the best things in life are free", "nail biting refreshes the feet", 12),
    Anagram("rome", "rome was not built in a day", "any labour i do wants time", 10),
    Anagram("meaning", "the meaning of life", "the fine game of nil", 10),
    Anagram("meaning_reverse", "the fine game of nil", "the meaning of life", 10),
    Anagram("schaffen", "wir schaffen das", "warne das schiff", 8),
  )

  val morpher = AnagramMorph.anagramMorphJumbled
  val justifier = Justify.justifyDefault

  val dirHome = new File(System.getProperty("user.home"))
  val dirOut = new File(dirHome, "tmp")

  anas.foreach { ana =>
    val lines = morpher.morph(ana.from, ana.to, ana.lines)
    val file = new File(dirOut, s"ana_${ana.name}.png")
    justifier.writePng(lines, file, 200)
  }

  println("finished anagram")

}
