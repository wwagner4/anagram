package anagram

import java.awt.image.BufferedImage
import java.awt.{Font, FontMetrics, Graphics2D, RenderingHints}
import java.io.File
import javax.imageio.ImageIO

object TryoutJustify extends App {

  val texte = List(
    "the meaning of life",
    "eaning of life them",
    "of life them eaning",
    "ofl them ife eaning",
    "hem ifee oflt aning",
    "mifeeo he taning fl",
    "ig amil eofn ef nthe",
    "nthei ga mi leofn ef",
    "ef nthe igam il eofn",
    "nthee fi game of nil",
    "fin the egame nil of",
    "the fine game of nil"
  )

  case class Word(
                   word: String,
                   wordWidth: Int
                 )

  case class WordOffset(
                         word: String,
                         xOffset: Int
                       )

  val fontSize = 200
  val font = new Font(Font.SANS_SERIF, Font.BOLD, fontSize)

  val bi1 = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY)
  val g1 = bi1.getGraphics.asInstanceOf[Graphics2D]
  g1.setFont(font)
  g1.setRenderingHint(
    RenderingHints.KEY_TEXT_ANTIALIASING,
    RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
  val fm1 = g1.getFontMetrics()

  val sentances: Seq[Seq[Word]] = for (t <- texte) yield words(t, fm1)
  val minSpace = fm1.stringWidth(" ")
  val maxLen = sentances.map { words =>
    sentanceMinLength(words, minSpace)
  }.max

  val xBorder = minSpace
  val height = (sentances.size * fontSize + fontSize * 0.4).toInt
  val width = maxLen + 2 * xBorder

  val sentancesOffset: Seq[Seq[WordOffset]] = wordOffsets(sentances, maxLen, minSpace)

  val bi2 = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY)
  val g2 = bi2.getGraphics.asInstanceOf[Graphics2D]
  g2.setFont(font)
  g2.setRenderingHint(
    RenderingHints.KEY_TEXT_ANTIALIASING,
    RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
  for ((line, i) <- sentancesOffset.zipWithIndex) {
    drawLine(line, g2, i, fontSize, xBorder)
  }

  val home = new File(System.getProperty("user.home"))
  val dirOut = new File(home, "tmp")

  val file = new File(dirOut, "t.png")
  ImageIO.write(bi2, "PNG", file)

  println("wrote to " + file)


  def wordOffsets(sentances: Seq[Seq[Word]], maxLen: Int, minSpace: Int): Seq[Seq[WordOffset]] = {
    sentances.map { sent =>
      val min = sentanceMinLength(sent, minSpace)
      val diff = maxLen - min
      val fillLen = diff / (sent.size - 1)
      var off = 0
      for (word <- sent) yield {
        val re = WordOffset(word.word, off)
        off += word.wordWidth + minSpace + fillLen
        re
      }
    }
  }

  def drawLine(line: Seq[WordOffset], g: Graphics2D, lineIndex: Int, fontSize: Int, xBorder: Int) = {
    for (word <- line) {
      g.drawString(word.word, word.xOffset + xBorder, fontSize * (lineIndex + 1))
    }
  }


  def sentanceMinLength(sent: Seq[Word], minSpace: Int): Int = {
    val spaceCnt: Int = sent.size - 1
    val wordsLen: Int = sent.foldLeft(0) { (a, b) => b.wordWidth + a }
    wordsLen + spaceCnt * minSpace
  }

  def words(txt: String, fm: FontMetrics): List[Word] = {
    for (word <- txt.split("\\s").toList) yield {
      val w: Int = fm.stringWidth(word)
      Word(word, w)
    }
  }


}
