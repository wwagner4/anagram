package anagram.solve

import scala.collection.{GenIterable, mutable}

class AnaCache {

  val map =  mutable.Map.empty[String, GenIterable[List[String]]]

  def addAna(txt: String, anas: GenIterable[List[String]]): Unit = {
    map.put(txt, anas)
  }

  def ana(txt: String): Option[GenIterable[List[String]]] = map.get(txt)

}
