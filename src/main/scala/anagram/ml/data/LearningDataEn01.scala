package anagram.ml.data

object LearningDataEn01 extends App {

  val booksEn01 = BookCollection(
    id = "en01",
    desc = "Collection of English Books #1",
    sentanceLength = 2 to 7,
    books = Seq(
      Book("books/ATaleofTwoCities.txt", "A Tale of Two Cities", "Charles Dickens"),
      Book("books/CommonSense.txt", "Common Sense", "Thomas Paine"),
      Book("books/StoriesbyEnglishAuthors.txt", "Stories by English Authors", "Various"),
      Book("books/TheAdventuresofTomSawyer.txt", "The Adventures of Tom Sawyer", "Mark Twain"),
      Book("books/ThePictureofDorianGray.txt", "The Picture of Dorian Gray", "Oscar Wilde"),
    )
  )

  LearningData.createData(booksEn01)

}
