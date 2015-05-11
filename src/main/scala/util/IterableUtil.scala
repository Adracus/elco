package util

/**
 * Created by axel on 11/05/15.
 */
object IterableUtil {
  implicit class RichIterable[+A](val iterable: Iterable[A]) {
    def firstWhere(pred: A => Boolean): Option[A] = {
      for (e <- iterable) {
        if(pred(e)) return Some(e)
      }
      None
    }
  }
}
