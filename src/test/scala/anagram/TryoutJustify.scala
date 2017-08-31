package anagram

import java.awt.image.BufferedImage
import java.awt.{Font, Graphics2D, RenderingHints}
import java.io.File
import javax.imageio.ImageIO

object TryoutJustify extends App {

  val texte = List(
    "durch die wüste zieht karawane",
    "der bankraub vom yppenplatz",
    "wie werbung durch big data ihre kunden kennenlernt",
    "immer wieder infantil",
    "das 40 jahre wolfi fest",
  )

  case class W(
                word: String,
                wordWidth: Int
              )

  case class W1(
                word: String,
                offset: Int
              )

  val bi = new BufferedImage(2000, 1000, BufferedImage.TYPE_BYTE_GRAY)

  val g = bi.getGraphics.asInstanceOf[Graphics2D]
  val f = new Font(Font.MONOSPACED, Font.BOLD, 77)
  val fm = g.getFontMetrics(f)
  val minSpace = fm.stringWidth(" ")
  g.setFont(f)
  g.setRenderingHint(
    RenderingHints.KEY_TEXT_ANTIALIASING,
    RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
  val t1: Seq[List[W]] = for(t <- texte) yield p(t)

  def sentanceMinLength(sent: List[W]): Int = {
    val spaceCnt: Int = sent.size - 1
    val wordsLen: Int = sent.foldLeft(0){(a, b) => b.wordWidth + a}
    wordsLen + spaceCnt * minSpace
  }

  val maxLen = t1.map{sentanceMinLength}.max


  def p(txt: String): List[W] = {
    for (word <- txt.split("\\S").toList) yield {
      val w: Int = fm.stringWidth(txt)
      W(word, w)
    }
  }

  // g.drawString(t, 0, off)

  val home = new File(System.getProperty("user.home"))
  val dirOut = new File(home, "tmp")

  val file = new File(dirOut, "t.png")
  ImageIO.write(bi, "PNG", file)

  println("wrote to " + file)
}
