package anagram

object Tryout extends App {

  //2 to 10 foreach{i => val li = (0 to (100, 100 / i)).toList ; println(li) }

  var m = Map(
    ("A" -> 1),
    ("C" -> 3),
    ("B" -> 2),
  )

  m = (("R" -> 10) :: m.toList).take(4).toMap
  m = (("X" -> 1) :: m.toList).take(4).toMap
  m = (("Y" -> -3) :: m.toList).take(4).toMap
  m = (("Z" -> 7) :: m.toList).take(4).toMap


}
