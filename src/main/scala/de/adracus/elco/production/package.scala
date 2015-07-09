package de.adracus.elco

import de.adracus.elco.ast.AstNode

/**
 * Created by axel on 08/07/15.
 */
package object ast_nodes {
  trait Expression

  case class Constant(value: Int) extends AstNode(value) with Expression

  case class Identifier(identifier: String) extends AstNode(identifier) with Expression

  abstract class Assignment(
      identifier: Identifier,
      expression: Expression)
    extends AstNode(identifier, expression) with Expression

  case class ValAssignment(identifier: Identifier, expression: Expression)
    extends Assignment(identifier, expression)

  case class VarAssignment(identifier: Identifier, expression: Expression)
    extends Assignment(identifier, expression)

  object Unit extends AstNode with Expression
}
