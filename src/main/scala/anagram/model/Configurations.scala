package anagram.model

import anagram.model.grm.CfgModelGrm
import anagram.model.grmcat.CfgModelGrmCat
import anagram.model.grmred.CfgModelGrmRed
import anagram.model.plain.CfgModelPlain
import anagram.model.plainrandom.CfgModelPlainRandom
import anagram.model.plainrated.CfgModelPlainRated

object Configurations {

  def all: Iterable[CfgModel[_]] = List(
    plain,
    plainRandom,
    plainRated,
    grammar,
    grammarReduced,
    grammarCategorized,
  )

  def plain: CfgModel[_] = new CfgModelPlain

  def plainRated: CfgModel[_] = new CfgModelPlainRated

  def plainRandom: CfgModel[_] = new CfgModelPlainRandom

  def grammar: CfgModel[_] = new CfgModelGrm

  def grammarReduced: CfgModel[_] = new CfgModelGrmRed

  def grammarCategorized: CfgModel[_] = new CfgModelGrmCat

}
