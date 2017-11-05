package anagram.common

trait SortedList[T] {

  def add(elem: T): Unit

  def take(n: Int): Seq[T]

  def size: Int

}

object SortedList {

  val MAX = 10000

  def instance[T](implicit ord: Ordering[T]): SortedList[T] = {
    new SortedList[T] {

      private var cnt = 0

      private var elems = Seq.empty[T]

      override def add(elem: T): Unit = synchronized {
        cnt += 1
        elems = (elems :+ elem).sorted.take(MAX)
      }

      override def take(n: Int): Seq[T] = {
        require(n <= MAX)
        elems.take(n)
      }

      override def size: Int = cnt

    }
  }

}
