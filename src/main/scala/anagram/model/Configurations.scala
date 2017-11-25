package anagram.model

import anagram.model.grm.CfgModelGrm
import anagram.model.grmred.CfgModelGrmRed
import anagram.model.plain.CfgModelPlain

object Configurations {

  def all: Iterable[CfgModel] = List(
    plain,
    grammar,
    grammarReduced,
  )

  def grammarReduced: CfgModel = new CfgModelGrmRed

  def grammar: CfgModel = new CfgModelGrm

  def plain: CfgModel = new CfgModelPlain

}
