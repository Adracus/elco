package util

/**
 * Created by axel on 11/05/15.
 */
object RichIterable {
  implicit class RichIterable[+A](val iterable: Iterable[A]) {
    def firstWhere(predicate: A => Boolean): Option[A] = {
      for (e <- iterable) {
        if(predicate(e)) return Some(e)
      }
      None
    }

    def lastMap[B](mapper: A => B, onLast: A => B) = {
      def recurse(acc: List[B], current: A, iterator: Iterator[A]): List[B] = {
        if (!iterator.hasNext) acc :+ onLast(current)
        else recurse(acc :+ mapper(current), iterator.next(), iterator)
      }

      val iterator = iterable.iterator
      recurse(List.empty, iterator.next(), iterator)
    }

    def firstMap[B](onFirst: A => B, mapper: A => B) = {
      def recurse(acc: List[B], iterator: Iterator[A]): List[B] = {
        if (iterator.hasNext) recurse(acc :+ mapper(iterator.next()), iterator)
        else acc
      }

      val iterator = iterable.iterator
      recurse(List(onFirst(iterator.next())), iterator)
    }
  }
}
