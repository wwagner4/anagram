package anagram.morph

import java.io.File

import anagram.morph.impl.JustifyImpl

object Justify {

  def justifyDefault: Justify = new JustifyImpl()

}

trait Justify {

  def writePng(lines: Seq[String], outFile: File, fontSize: Int): Unit

}
