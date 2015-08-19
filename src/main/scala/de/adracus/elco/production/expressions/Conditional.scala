package de.adracus.elco.production.expressions

/**
 * Created by axel on 19/08/15.
 */
case class Conditional(condition: Expression, ifBody: Expression, elseBody: Option[Expression])
  extends Expression(condition, ifBody, elseBody)
