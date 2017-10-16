package anagram.ml.data.gcide

import org.scalatest.{FunSuite, MustMatchers}

class GcideTransformSuite  extends FunSuite with MustMatchers {

  val wordsList = Seq(
    ("<p><ent>Allegorization</ent><br/", "allegorization"),
    ("<p><ent>Allegro</ent><br/", "allegro"),
    ("<p><ent>Alley</ent><br/", "alley"),
    ("<p><ent>Aloetic</ent><br/", "aloetic"),
  )

  for ((line, word) <- wordsList) {
    test(s"extract word $word") {
      GcideTransfom.extractWord(line) mustBe Some(word)
    }
  }

  val noWordList = Seq(
    "<p><ent>all-powerful</ent><br/",
    "<p><ent>all right</ent><br/",
    "<p><ent>All Souls' Day</ent><br/",
    """<hw>All\" Souls' Day`</hw> <pr>(<?/)</pr>. <def>The second day of November; a feast day of the Roman Catholic church, on which supplications are made for the souls of the faithful dead.</def><br/""",
    """[<source>1913 Webster</source>]</p>""",
    """It has been supposed to combine the flavor of cinnamon, nutmegs, and cloves; and hence the name. The name is also given to other aromatic shrubs; as, the <stype>Carolina allspice</stype> (<spn>Calycanthus floridus</spn>);""",
    """<stype>wild allspice</stype> (<spn>Lindera benzoin</spn>), called also <altname>spicebush</altname>, <altname>spicewood</altname>, and <altname>feverbush</altname>.</def><br/""",
    """""",
  )

  for ((line, i) <- noWordList.zipWithIndex) {
    test(s"do not extract word $i") {
      GcideTransfom.extractWord(line) mustBe None
    }
  }

  val wtypeList = Seq(
    ("a", """<hw>Am`a*to"ri*an</hw> <pr>(<?/)</pr>, <pos>a.</pos> <def>Amatory.</def> <mark>[R.]</mark>  <rj><au>Johnson.</au></rj><br/"""),
    ("a1", """<hw>Am"a*to*ry</hw> <pr>(<?/)</pr>, <pos>a.1</pos> <def>Pertaining to, producing, or expressing, sexual love; <as>as, <ex>amatory</ex> potions</as>.</def><br/"""),
    ("vt", """<hw>A*maze"</hw> <pr>(<?/)</pr>, <pos>v. t.</pos> <vmorph>[<pos>imp. & p. p.</pos> <conjf>Amazed</conjf> <pr>(<?/)</pr>; <pos>p. pr. & vb. n.</pos> <conjf>Amazing</conjf>.]</vmorph> <ety>[Pref. <ets>a-</ets> + <ets>maze</"""),
    ("n", """<mhw>{ \'d8<hw>am`bly*o"pi*a</hw> <pr>(<acr/m`bl<esl/*<omac/"p<esl/*<adot/)</pr>, <hw>am"bly*o`py</hw> <pr>(<acr/m"bl<esl/*<omac/`p<ycr/)</pr>, }</mhw> <pos>n.</pos> <ety>[Gr. <grk>'amblywpi`a</grk>; <grk>'ambly`"""),
  )

  for ((ty, line) <- wtypeList) {
    test(s"extract word type $ty") {
      GcideTransfom.extractWtype(line) mustBe Some(ty)
    }
  }

  val noWtypeList = Seq(
    """<p><ent>Allegorization</ent><br/""",
    """p><ent>Allegro</ent><br/""",
    """<p><ent>Alley</ent><br/""",
    """<p><ent>Aloetic</ent><br/""",
    """""",
    """blunt, dim + <grk>'w`ps</grk> eye: cf. F. <ets>amblyopie</ets>.]</ety> <fld>(Med.)</fld> <def>Weakness of sight, without any detectable organic lesion.</def><br/""",
    """[<source>WordNet 1.5</source>]</p>""",
  )

  for ((line, i) <- noWtypeList.zipWithIndex) {
    test(s"do not extract word type $i") {
      GcideTransfom.extractWtype(line) mustBe None
    }
  }

}
