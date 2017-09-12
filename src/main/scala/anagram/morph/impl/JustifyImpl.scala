package anagram.morph.impl

import java.awt.{Color, Font, FontMetrics, Graphics2D, RenderingHints}
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import anagram.morph.Justify

class JustifyImpl extends Justify {

  // Win
  // val fontName = "Bradley Hand ITC"
  // val fontName = "Forte"
  // val fontName = "MV Boli"

  // Mac
  // val fontName = "Bradley Hand"
  // val fontName = "Gill Sans"
  val fontName = "Skia"

  // Java
  // val fontName: String = Font.DIALOG

  case class Word(
                   word: String,
                   wordWidth: Int
                 )

  case class WordOffset(
                         word: String,
                         xOffset: Int
                       )


  def writePng(lines: Seq[String], outFile: File, fontSize: Int): Unit = {

    val font = new Font(fontName, Font.BOLD, fontSize)

    val bi1 = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY)
    val g1 = bi1.getGraphics.asInstanceOf[Graphics2D]
    g1.setFont(font)
    g1.setRenderingHint(
      RenderingHints.KEY_TEXT_ANTIALIASING,
      RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
    val fm1 = g1.getFontMetrics()

    val sentances: Seq[Seq[Word]] = for (t <- lines) yield words(t, fm1)
    val minSpace = fm1.stringWidth(" ")
    val maxLen = sentances.map { words =>
      sentanceMinLength(words, minSpace)
    }.max

    val xBorder = minSpace
    val height = (sentances.size * fontSize + fontSize * 0.4).toInt
    val width = maxLen + 2 * xBorder

    val sentancesOffset: Seq[Seq[WordOffset]] = wordOffsets(sentances, maxLen, minSpace)

    val bi2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val g2 = bi2.getGraphics.asInstanceOf[Graphics2D]
    g2.setColor(Color.BLACK)
    g2.setFont(font)
    g2.setRenderingHint(
      RenderingHints.KEY_TEXT_ANTIALIASING,
      RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
    for ((line, i) <- sentancesOffset.zipWithIndex) {
      drawLine(line, g2, i, fontSize, xBorder)
    }

    ImageIO.write(bi2, "PNG", outFile)

    println("wrote to " + outFile)


  }


  def words(txt: String, fm: FontMetrics): List[Word] = {
    for (word <- txt.split("\\s").toList) yield {
      val w: Int = fm.stringWidth(word)
      Word(word, w)
    }
  }

  def wordOffsets(sentances: Seq[Seq[Word]], maxLen: Int, minSpace: Int): Seq[Seq[WordOffset]] = {
    sentances.map { sent =>
      if (sent.size == 1) {
        Seq(WordOffset(sent(0).word, 0))
      } else {
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
  }

  def drawLine(line: Seq[WordOffset], g: Graphics2D, lineIndex: Int, fontSize: Int, xBorder: Int): Unit = {
    for (word <- line) {
      g.drawString(word.word, word.xOffset + xBorder, fontSize * (lineIndex + 1))
    }
  }

  def sentanceMinLength(sent: Seq[Word], minSpace: Int): Int = {
    val spaceCnt: Int = sent.size - 1
    val wordsLen: Int = sent.foldLeft(0) { (a, b) => b.wordWidth + a }
    wordsLen + spaceCnt * minSpace
  }

}
