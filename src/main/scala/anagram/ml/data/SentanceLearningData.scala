package anagram.ml.data

case class Book(
                 filename: String,
                 title: String,
                 author: String,
               )

case class BookCollection(
                           id: String,
                           desc: String,
                           books: Seq[Book],
                         )


class SentanceLearningData {

  val booksEn01 = BookCollection(
    id = "en01",
    desc = "English Books Nr. 01",
    books = Seq(
      Book("ATaleofTwoCities.txt", "A Tale of Two Cities", "Charles Dickens"),
      Book("CommonSense.txt", "Common Sense", "Thomas Paine"),
      Book("StoriesbyEnglishAuthors.txt", "Stories by English Authors", "Various"),
      Book("TheAdventuresofTomSawyer.txt", "The Adventures of Tom Sawyer", "Mark Twain"),
      Book("ThePictureofDorianGray.txt", "The Picture of Dorian Gray", "Oscar Wilde"),
    )
  )

  val booksTwoLines = BookCollection(
    id = "tl",
    desc = "Testset with one book containing only two lines",
    books = Seq(
      Book("TwoLines.txt", "TwoLines", "Test"),
    )
  )





}
