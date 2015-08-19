package de.adracus.elco.production.expressions

/**
 * Created by axel on 19/08/15.
 */
case class FunctionDefinition(identifier: String, identifiers: IdentifierList, body: ExpressionList)
  extends Expression(identifier, identifiers, body)
