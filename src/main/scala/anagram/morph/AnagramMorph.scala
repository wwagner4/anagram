package anagram.morph

import anagram.morph.impl.{AnagramMorphJumbled, AnagramMorphLinear}

object AnagramMorph {

  def anagramMorphJumbled: AnagramMorph = new AnagramMorphJumbled
  def anagramMorphLinear: AnagramMorph = new AnagramMorphLinear

}


trait AnagramMorph {

  def morph(from: String, to: String, lines: Int): Seq[String]

}
