package anagram

import java.io.File

object TryoutJustify extends App {

  val texte = List(
    "the meaning of life",
    "eaning of life them",
    "of life them eaning",
    "ofl them ife eaning",
    "hem ifee oflt aning",
    "mifeeo he taning fl",
    "ig amil eofn ef nthe",
    "nthei ga mi leofn ef",
    "ef nthe igam il eofn",
    "nthee fi game of nil",
    "fin the egame nil of",
    "the fine game of nil"
  )

  val home = new File(System.getProperty("user.home"))
  val dirOut = new File(home, "tmp")
  val file = new File(dirOut, "t.png")

  Justify.justifyDefault.writePng(texte, file, 200)


}
