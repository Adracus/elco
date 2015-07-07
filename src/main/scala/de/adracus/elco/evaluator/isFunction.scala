package de.adracus.elco.evaluator

/**
 * Created by axel on 01/07/15.
 */
object isFunction {
  def apply(any: Any) = any match {
    case f: (() => Any) => true
    case f: ((_) => Any) => true
    case f: ((_, _) => Any) => true

    case _ => false
  }
}
