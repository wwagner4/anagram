package anagram.solve

import scala.collection.GenIterable

class AnaCache {

  var map =  Map.empty[String, GenIterable[List[String]]]

  def addAna(txt: String, anas: GenIterable[List[String]]): Unit = synchronized {
    map = ((txt -> anas) :: map.toList).take(100000).toMap
  }

  def ana(txt: String): Option[GenIterable[List[String]]] = map.get(txt)

}
