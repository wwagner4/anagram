package anagram.model.grmred

import anagram.common.IoUtil
import anagram.words.{Word, Wordlists}

object GrouperTryout extends App {

  val wl = Wordlists.grammarReducedRated.wordList()

  for ((w, i) <- wl.take(10).zipWithIndex) {
    println(w)
  }

  def create(): Unit = {

    val wlRated: Iterable[Word] = Wordlists.plainRatedLarge.wordList()
    val wlGrammar: Iterable[Word] = Wordlists.grammar.wordList()

    val grp = new GrouperGrmRed(wlGrammar)

    val wlGrammarRed: Iterable[Word] = wlGrammar.map{w =>
      val g = grp.group(w.word)(0)
      w.copy(grp = Some(g))
    }

    val mapRated: Map[String, Word] = wlRated.map(w => (w.word, w)).toMap

    val wlAll = wlGrammarRed.flatMap {
      w => mapRated.get(w.word).map(wr => wr.copy(grp = w.grp))
    }

    println(s"size of wlRated ${wlRated.size}")
    println(s"size of wlGrammar ${wlGrammar.size}")
    println(s"size of wlGrammarRed ${wlGrammarRed.size}")
    println(s"size of wlAll ${wlAll.size}")

    IoUtil.save(IoUtil.dirOut, "wordlist_grammar_rated.txt", {bw =>
      for(w <- wlAll) {
        bw.write(s"${w.word};${w.wordSorted};${w.grp.get};${w.rating.get}\n")
      }
    })

    println (s"Wrote to wordlist_grammar_rated.txt")
  }
}
