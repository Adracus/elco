package de.adracus.elco.evaluator

/**
 * Created by axel on 29/06/15.
 */
object wrap {
  def apply(any: AnyRef, before: Seq[Any] => Unit, after: (Any, Seq[Any]) => Unit) = any match {
    case f: (() => Any) =>
      () => {
        val args = Seq.empty[Any]
        before(args)
        val res = f()
        after(res, args)
        res
      }
    case f: ((Any) => Any) =>
      (a: Any) => {
        val args = Seq(a)
        before(args)
        val res = f(a)
        after(res, args)
        res
      }
    case f: ((Any, Any) => Any) =>
      (a: Any, b: Any) => {
        val args = Seq(a, b)
        before(args)
        val res = f(a, b)
        after(res, args)
        res
      }
    case f: ((Any, Any, Any) => Any) =>
      (a: Any, b: Any, c: Any) => {
        val args = Seq(a, b, c)
        before(args)
        val res = f(a, b, c)
        after(res, args)
        res
      }
  }

  implicit class Wrap(val any: Any) {
    def wrapped(before: Seq[Any] => Unit, after: (Any, Seq[Any]) => Unit) =
      wrap(any.asInstanceOf[AnyRef], before, after)
  }
}
