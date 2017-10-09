package anagram.ml

import anagram.ml.data._
import anagram.ml.train.Training

object Main extends App {

  val dataId = "en03"
  val createData = true

  val wordMapper = WordMap.createWordMapFromWordlistResource("wordlist/wordlist_small.txt")

  val books = BookCollection(
    id = dataId,
    desc = "Collection of English Books #2",
    sentenceLength = 2 to 7,
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
      Book("books/UnderGreekSkies.txt", "Under Greek Skies", "Julia D. Dragoumis"),
    )
  )

  val sentenceCreator = new SentenceCreatorSliding()

  if (createData) new LearningData(wordMapper, sentenceCreator).createData(books)
  Training.train(dataId)

}
