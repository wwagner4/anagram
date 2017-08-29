package anagram

import java.awt.{BorderLayout, Canvas, Dimension, Font, FontMetrics, Graphics2D, Panel, RenderingHints}
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFrame

object TryoutJustify extends App {

  val texte = List(
    ("durch die wüste zieht karawane", 100),
    ("der bankraub vom yppenplatz", 150),
    ("wie werbung durch big data ihre kunden kennenlernt", 200),
    ("immer wieder infantil", 250),
    ("das 40 jahre wolfi fest", 300),
  )

  val bi = new BufferedImage(2000, 1000, BufferedImage.TYPE_BYTE_GRAY)

  val g = bi.getGraphics.asInstanceOf[Graphics2D]
  val f = new Font(Font.MONOSPACED, Font.BOLD, 77)
  g.setFont(f)
  g.setRenderingHint(
    RenderingHints.KEY_TEXT_ANTIALIASING,
    RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
  for ((t, off) <- texte) {
    g.drawString(t, 0, off)
  }

  val home = new File(System.getProperty("user.home"))
  val dirOut = new File(home, "tmp")

  val file = new File(dirOut, "t.png")
  ImageIO.write(bi, "PNG", file)

  println("wrote to " + file)
}
