package de.adracus.elco.evaluator

/**
 * Created by axel on 29/06/15.
 */
object makeFunction {
  def apply(arity: Int)(body: IndexedSeq[Any] => Any) = arity match {
    case 0 => () => body(IndexedSeq.empty[Any])
    case 1 => (a: Any) => body(IndexedSeq(a))
    case 2 => (a: Any, b: Any) => body(IndexedSeq(a, b))
    case 3 => (a: Any, b: Any, c: Any) => body(IndexedSeq(a, b, c))
    case 4 => (a: Any, b: Any, c: Any, d: Any) => body(IndexedSeq(a, b, c, d))
  }
}
