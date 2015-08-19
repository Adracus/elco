package de.adracus.elco.production.expressions

/**
 * Created by axel on 19/08/15.
 */
case class ExpressionList(expressions: List[Expression]) extends Expression(expressions:_*) {
  def :+(expression: Expression) = ExpressionList(expressions :+ expression)
}

object ExpressionList {
  def single(expression: Expression) = ExpressionList(List(expression))
}
