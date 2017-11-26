package anagram.ml.data.common

object BookCollections {

  val bookSmallRes = "books/TwoLines.txt"

  val collectionOneBook = BookCollection(
    desc = "Collection of English Books #1",
    books = Seq(
      Book("books/CommonSense.txt", "Common Sense", "Thomas Paine"),
    )
  )

  val collectionEn1 = BookCollection(
    desc = "Collection of English Books #1",
    books = Seq(
      Book("books/ATaleofTwoCities.txt", "A Tale of Two Cities", "Charles Dickens"),
      Book("books/CommonSense.txt", "Common Sense", "Thomas Paine"),
      Book("books/StoriesbyEnglishAuthors.txt", "Stories by English Authors", "Various"),
      Book("books/TheAdventuresofTomSawyer.txt", "The Adventures of Tom Sawyer", "Mark Twain"),
      Book("books/ThePictureofDorianGray.txt", "The Picture of Dorian Gray", "Oscar Wilde"),
    )
  )

  val collectionEn2 = BookCollection(
    desc = "Collection of English Books #2",
    books = Seq(
      Book("books/ATaleofTwoCities.txt", "A Tale of Two Cities", "Charles Dickens"),
      Book("books/CommonSense.txt", "Common Sense", "Thomas Paine"),
      Book("books/StoriesbyEnglishAuthors.txt", "Stories by English Authors", "Various"),
      Book("books/TheAdventuresofTomSawyer.txt", "The Adventures of Tom Sawyer", "Mark Twain"),
      Book("books/ThePictureofDorianGray.txt", "The Picture of Dorian Gray", "Oscar Wilde"),
      Book("books/StoriesInVerse.txt", "Stories in Verse", "Henry Abbey"),
      Book("books/CampingAtCherryPond.txt", "Camping at Cherry Pond", "Henry Abbott"),
      Book("books/StateOfTheUnionAddresses.txt", "State of the Union Addresses", "Ronald Reagan"),
      Book("books/ThePeoplesOfIndia.txt", "The Peoples of India", "James Drummond Anderson"),
      Book("books/OperationHaystack.txt", "Operation Haystic", "Frank Herbert"),
      Book("books/Sanine.txt", "Sanine", "Artsybashev"),
      Book("books/UnderGreekSkies.txt", "Under Greek Skies", "Julia D. Dragoumis"),
      Book("books/TheLureOfTheMask.txt", "The Lure of the Mask", "Mac Grath"),
      Book("books/TreasureHunters.txt", "Treasure Hunters", "Unknown"),
      Book("books/SomethingChildishAndOtherStories.txt", "Something Childish and Other Stories", "Katherine Mansfield"),
      Book("books/TheSignOfTheSpider.txt", "The Sign of the Spider", "Bertram Mitford"),
      Book("books/AGoodCleanAHarshClean.txt", "A good clean, a harsh clean", "Brian Martinez"),
      Book("books/FabulousFancies.txt", "Fabulous Fancies", "William Babington Maxwell"),
    )
  )

  val collectionTwoLines = BookCollection(
    desc = "Testset with one book containing only two lines",
    books = Seq(
      Book("books/TwoLines.txt", "TwoLines", "Test"),
    )
  )

}

case class Book(
                 filename: String,
                 title: String,
                 author: String,
               )

case class BookCollection(
                           desc: String,
                           books: Seq[Book],
                         )

