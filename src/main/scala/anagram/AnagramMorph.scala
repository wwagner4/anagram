package anagram

import anagram.impl.AnagramMorphJumbled

object AnagramMorph {

  def anagramMorphJumbled: AnagramMorph = new AnagramMorphJumbled

}


trait AnagramMorph {

  def morph(from: String, to: String, lines: Int): List[String]

}
