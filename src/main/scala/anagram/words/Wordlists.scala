package anagram.words

import java.nio.file.Paths

import anagram.common.IoUtil

object Wordlists {

  val prefix = "wordlist/wordlist"

  def plainFreq2k: WordListFactory = createFactory("Books 2k", "B2K", () => loadWordlistPlainFreq(s"${prefix}_books_frequency_2000.txt"))

  def plainFreq3k: WordListFactory = createFactory("Books 3k", "B3k", () => loadWordlistPlainFreq(s"${prefix}_books_frequency_3000.txt"))

  def plainFreq5k: WordListFactory = createFactory("Books 5k", "B5k", () => loadWordlistPlainFreq(s"${prefix}_books_frequency_5000.txt"))

  def plainFreq10k: WordListFactory = createFactory("Books 10k", "B10K", () => loadWordlistPlainFreq(s"${prefix}_books_frequency_10000.txt"))

  def plainFreq50k: WordListFactory = createFactory("Books 50k", "B50k", () => loadWordlistPlainFreq(s"${prefix}_books_frequency_50000.txt"))

  def plainFreq100k: WordListFactory = createFactory("Books 100k", "B100k", () => loadWordlistPlainFreq(s"${prefix}_books_frequency_100000.txt"))

  def plain: WordListFactory = createFactory("Plain small", "PLAIN_S", () => loadWordlistPlain(s"${prefix}_small.txt"))

  def grammar: WordListFactory = createFactory("Grammar full", "GRM_L", () => loadWordlistGrammar(s"${prefix}_grammar_full.txt"))

  private def createFactory(desc: String, sd: String, f: () => Iterable[Word]): WordListFactory = {

    new WordListFactory {
      override def wordList: () => Iterable[Word] = f

      override def description: String = desc

      override def shortSescription: String = sd
    }
  }

  private def loadWordlistGrammar(str: String): scala.Iterable[Word] = {
    def lineToWord(line: String): Word = {
      val _s = line.split(';')
      Word(_s(1), _s(1).sorted, Some(_s(0)), None)
    }

    IoUtil.loadTxtFromPath(Paths.get(IoUtil.uri(str)), (l) => l.toIterable)
      .map(line => lineToWord(line))
  }

  private def loadWordlistPlain(str: String): scala.Iterable[Word] = {
    IoUtil.loadTxtFromPath(Paths.get(IoUtil.uri(str)), (l) => l.toIterable)
      .map(line => Word(line, line.sorted))
  }

  private def loadWordlistPlainFreq(str: String): scala.Iterable[Word] = {
    def lineToWord(line: String): Word = {
      val _s = line.split(';')
      Word(_s(0), _s(0).sorted, None, Some(_s(1).toInt))
    }

    IoUtil.loadTxtFromPath(Paths.get(IoUtil.uri(str)), (l) => l.toIterable)
      .map(line => lineToWord(line))
  }

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
    "lbs",
    "blvd",
    "bldg",
    "dibs",
    "dib",
  ).toSet
}
