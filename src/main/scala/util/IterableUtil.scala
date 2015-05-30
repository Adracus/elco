package util

/**
 * Created by axel on 11/05/15.
 */
object IterableUtil {
  implicit class RichIterable[+A](val iterable: Iterable[A]) {
    def firstWhere(predicate: A => Boolean): Option[A] = {
      for (e <- iterable) {
        if(predicate(e)) return Some(e)
      }
      None
    }
  }
}
