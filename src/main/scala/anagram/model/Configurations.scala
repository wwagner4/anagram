package anagram.model

import anagram.model.grm.CfgModelGrm
import anagram.model.grmred.CfgModelGrmRed
import anagram.model.plain.CfgModelPlain
import anagram.model.plainrandom.CfgModelPlainRandom

object Configurations {

  def all: Iterable[CfgModel] = List(
    plain,
    plainRandom,
    grammar,
    //grammarReduced,
  )

  def grammarReduced: CfgModel = new CfgModelGrmRed

  def grammar: CfgModel = new CfgModelGrm

  def plain: CfgModel = new CfgModelPlain

  def plainRandom: CfgModel = new CfgModelPlainRandom

}
