package anagram.words

import java.nio.file.Paths

import anagram.common.IoUtil

object Wordlists {

  val prefix = "wordlist/wordlist"

  def plainFreq2k: Iterable[Word] = loadWordlistPlainFreq(s"${prefix}_books_frequency_2000.txt")

  def plainFreq3k: Iterable[Word] = loadWordlistPlainFreq(s"${prefix}_books_frequency_3000.txt")

  def plainFreq5k: Iterable[Word] = loadWordlistPlainFreq(s"${prefix}_books_frequency_5000.txt")

  def plainFreq10k: Iterable[Word] = loadWordlistPlainFreq(s"${prefix}_books_frequency_10000.txt")

  def plainFreq50k: Iterable[Word] = loadWordlistPlainFreq(s"${prefix}_books_frequency_50000.txt")

  def plainFreq100k: Iterable[Word] = loadWordlistPlainFreq(s"${prefix}_books_frequency_100000.txt")

  def plain: Iterable[Word] = loadWordlistPlain(s"${prefix}_small.txt")

  def grammar: Iterable[Word] = loadWordlistGrammar(s"${prefix}_grammar_full.txt")

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
