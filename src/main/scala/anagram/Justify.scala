package anagram

import java.io.File

import anagram.impl.JustifyImpl

object Justify {

  def justifyDefault: Justify = new JustifyImpl()

}

trait Justify {

  def writePng(lines: Seq[String], outFile: File, fontSize: Int): Unit

}
