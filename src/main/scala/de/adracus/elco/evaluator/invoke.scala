package de.adracus.elco.evaluator

/**
 * Created by axel on 29/06/15.
 */
object invoke {
  def apply(any: Any, args: List[Any]) = any match {
    case f: (() => Any) => f()
    case f: ((Any) => Any) => f(args.head)
    case f: ((Any, Any) => Any) => f(args.head, args(1))
    case f: ((Any, Any, Any) => Any) => f(args.head, args(1), args(2))
  }

  implicit class Invoke(val any: Any) {
    def apply(args: List[Any]) = invoke.this(any.asInstanceOf[AnyRef], args)
  }
}