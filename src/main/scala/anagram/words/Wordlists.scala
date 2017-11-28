package anagram.words

import anagram.common.IoUtil

object Wordlists {

  val prefix = "wordlist/wordlist"

  def plainFreq2k: WordListFactory = createFactory("Books 2k", "B2K", () => loadWordlistPlainFreq(s"${prefix}_books_frequency_2000.txt"))

  def plainFreq3k: WordListFactory = createFactory("Books 3k", "B3k", () => loadWordlistPlainFreq(s"${prefix}_books_frequency_3000.txt"))

  def plainFreq5k: WordListFactory = createFactory("Books 5k", "B5k", () => loadWordlistPlainFreq(s"${prefix}_books_frequency_5000.txt"))

  def plainFreq10k: WordListFactory = createFactory("Books 10k", "B10K", () => loadWordlistPlainFreq(s"${prefix}_books_frequency_10000.txt"))

  def plainFreq30k: WordListFactory = createFactory("Books 30k", "B30k", () => loadWordlistPlainFreq(s"${prefix}_books_frequency_30000.txt"))

  def plain: WordListFactory = createFactory("Plain small", "PLAIN_S", () => loadWordlistPlain(s"${prefix}_small.txt"))

  def grammar: WordListFactory = createFactory("Grammar full", "GRM_L", () => loadWordlistGrammar(s"${prefix}_grammar_full.txt"))

  private def createFactory(desc: String, sd: String, f: () => Iterable[Word]): WordListFactory = {

    new WordListFactory {
      override def wordList: () => Iterable[Word] = f

      override def description: String = desc

      override def shortSescription: String = sd
    }
  }

  private def loadWordlistGrammar(resourceName: String): scala.Iterable[Word] = {
    def lineToWord(line: String): Word = {
      val _s = line.split(';')
      Word(_s(1), _s(1).sorted, Some(_s(0)), None)
    }

    IoUtil.loadTxtFromResourceName(resourceName, (l) => l.toIterable)
      .map(line => lineToWord(line))
  }

  private def loadWordlistPlain(resName: String): scala.Iterable[Word] = {
    def word(line: String): Option[Word] = if (ignoreWords.contains(line)) None else Some(Word(line, line.sorted))

    IoUtil.loadTxtFromResourceName(resName, (l) => l.toIterable)
      .flatMap(word)
  }

  private def loadWordlistPlainFreq(resName: String): scala.Iterable[Word] = {
    def lineToWord(line: String): Option[Word] = {
      val _s = line.split(';')
      if (ignoreWords.contains(_s(0))) None
      else Some(Word(_s(0), _s(0).sorted, None, Some(_s(1).toInt)))
    }

    IoUtil.loadTxtFromResourceName(resName, (l) => l.toIterable)
      .flatMap(line => lineToWord(line))
  }

  val ignoreWords = Set(
    "b",
    "c",
    "d",
    "e",
    "f",
    "g",
    "h",
    "j",
    "k",
    "l",
    "m",
    "n",
    "o",
    "p",
    "q",
    "r",
    "s",
    "t",
    "u",
    "v",
    "w",
    "x",
    "y",
    "z",
    "ah",
    "cd",
    "de",
    "el",
    "id",
    "la",
    "le",
    "lo",
    "na",
    "oh",
    "st",
    "ahl",
    "alt",
    "bin",
    "cal",
    "chi",
    "cia",
    "cit",
    "dib",
    "din",
    "ere",
    "esc",
    "etc",
    "inc",
    "lbs",
    "led",
    "nil",
    "nth",
    "ohs",
    "von",
    "attn",
    "bldg",
    "blvd",
    "brin",
    "dibs",
    "dict",
    "dows",
    "dreg",
    "ghee",
    "lith",
    "nevi",
    "noir",
    "odic",
    "orth",
    "thor",
    "vade",
    "veda",
    "veer",
    "wots",
    "vinal",
    "wonts",
    "hm",
    "mon",
    "ole",
    "em",
    "th",
    "ho",
    "ha",
    "eh",
    "ont",
    "al",
    "eh",
    "ive",
    "tis",
    "wot",
  )
}

object SortWords extends App {

  import anagram.words.Wordlists.ignoreWords

  ignoreWords
    .toSeq
    .sorted
    .sortBy(_.length)
    .foreach(w => println(s"""    "$w","""))
}


