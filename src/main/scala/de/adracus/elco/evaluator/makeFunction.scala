package de.adracus.elco.evaluator

/**
 * Created by axel on 29/06/15.
 */
object makeFunction {
  def apply(arity: Int, body: () => Any) = arity match {
    case 0 => () => body()
    case 1 => (a: Any) => body()
    case 2 => (a: Any, b: Any) => body()
    case 3 => (a: Any, b: Any, c: Any) => body()
    case 4 => (a: Any, b: Any, c: Any, d: Any) => body()
  }
}
