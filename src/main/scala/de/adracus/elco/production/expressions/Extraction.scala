package de.adracus.elco.production.expressions

/**
 * Created by axel on 19/08/15.
 */
case class Extraction(expression: Expression, identifier: String)
  extends Expression(expression, identifier)
