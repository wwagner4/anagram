package anagram.solve

import scala.collection.mutable

class AnaCache {

  var map =  mutable.Map.empty[String, List[List[String]]]

  def addAna(txt: String, anas: List[List[String]]): Unit = {
    map.put(txt, anas)
  }

  def ana(txt: String): Option[List[List[String]]] = map.get(txt)

}
