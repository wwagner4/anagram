package anagram.ml.data

object LearningDataTwoLines extends App {

  val books = BookCollection(
    id = "twoLinesTest",
    desc = "Testset with one book containing only two lines",
    sentenceLength = 2 to 3,
    books = Seq(
      Book("books/TwoLines.txt", "TwoLines", "Test"),
    )
  )
  val wm = WordMap.createWordMapFromWordlistResource("wordlist/wordlist_small.txt")
  new LearningData(wm).createData(books)

}
