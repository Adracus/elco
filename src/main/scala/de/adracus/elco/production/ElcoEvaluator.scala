package de.adracus.elco.production

import de.adracus.elco.ast.AstNode
import de.adracus.elco.evaluate.Evaluator

/**
 * Created by axel on 15/07/15.
 */
class ElcoEvaluator extends Evaluator[String] {
  def evaluate(node: AstNode) = node match {
    case ExpressionList(expressions) =>
      expressions.map(_.evaluate()).last

    case Constant(value) => value

    case VarAssignment(identifier, expression) =>
      stack.mut(identifier, expression.evaluate())
      ()

    case ValAssignment(identifier, expression) =>
      stack.const(identifier, expression)
      ()

    case VariableAccess(identifier) =>
      stack(identifier)
  }
}
