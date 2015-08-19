package de.adracus.elco.production.expressions

/**
 * Created by axel on 19/08/15.
 */
abstract class Assignment(
                           ident: String,
                           expr: Expression)
  extends Expression(ident, expr) {

  def identifier: String

  def expression: Expression
}

case class ValAssignment(identifier: String, expression: Expression)
  extends Assignment(identifier, expression)

case class VarAssignment(identifier: String, expression: Expression)
  extends Assignment(identifier, expression)
