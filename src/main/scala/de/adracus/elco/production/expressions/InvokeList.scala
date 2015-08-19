package de.adracus.elco.production.expressions

/**
 * Created by axel on 19/08/15.
 */
case class InvokeList(expressions: List[Expression]) extends Expression(expressions:_*) {
  def :+(expression: Expression) = InvokeList(expressions :+ expression)
}

object InvokeList {
  def single(expression: Expression) = InvokeList(List(expression))
  def empty() = InvokeList(List())
}
