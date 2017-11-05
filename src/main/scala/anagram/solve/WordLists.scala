package anagram.solve
import anagram.words.{Word, WordMappers}

object WordLists {

  lazy val wordListIgnoring: Iterable[Word] = {
    val ignoreWords = Seq(
      "ere",
      "nth",
      "id",
      "dreg",
      "cal",
      "inc",
      "nevi",
      "von",
      "cit",
      "esc",
      "alt",
      "brin",
      "veer",
      "brin",
      "bin",
      "nil",
      "chi",
      "cd",
      "ohs",
      "lith",
      "noir",
      "veda",
      "vade",
      "vinal",
      "dict",
      "wonts",
      "wots",
      "odic",
      "orth",
      "dows",
      "thor",
      "ghee",
      "attn",
      "din",
      "led",
      "etc",
      "cia",
    ).toSet

    WordMappers.createWordMapperPlain
      .wordList
      .filter(w => !ignoreWords.contains(w.word))
  }

}
