package anagram.model.plain

import anagram.words.{Word, WordRandom}

import scala.util.Random

class WordRandomPlain(wordList: Iterable[Word]) extends WordRandom {

  private val wa = wordList.map(_.word).toArray
  private val size = wa.length
  private val ran = new  Random

  override def random: String = {
    val i = ran.nextInt(size)
    wa(i)
  }

}
