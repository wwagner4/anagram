package anagram.common

trait SortedList[T] {

  def add(elem: T): Unit

  def take(n: Int): Seq[T]

  def size: Int

}

object SortedList {

  def instance[T](implicit ord: Ordering[T]): SortedList[T] = {
    new SortedList[T] {

      private var elems = Seq.empty[T]

      override def add(elem: T): Unit = synchronized {
        elems = (elems :+ elem).sorted
      }

      override def take(n: Int): Seq[T] = elems.take(n)

      override def size: Int = elems.size

    }
  }

}
