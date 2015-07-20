package de.adracus.elco.production

import de.adracus.elco.ast.AstNode
import de.adracus.elco.base.Method
import de.adracus.elco.evaluate.Evaluator

/**
 * Created by axel on 15/07/15.
 */
class ElcoEvaluator extends Evaluator[String] {
  def evaluate(node: AstNode) = node match {
    case ExpressionList(expressions) =>
      expressions.map(_.evaluate()).last

    case Constant(value) => value

    case FunctionDefinition(name, identifiers, body) =>
      val method = Method.createMethod(this.stack, body.evaluateFn, identifiers.identifiers)
      stack.const(name, method)
      method

    case FunctionCall(name, invokeList) =>
      val fn = stack(name).asInstanceOf[List[Any] => Any]
      fn(invokeList.expressions.map(_.evaluate()))

    case a: Assignment =>
      val value = a.expression.evaluate()
      a match {
        case VarAssignment(identifier, _) =>
          stack.mut(identifier, value)

        case ValAssignment(identifier, _) =>
          stack.const(identifier, value)
      }
      value

    case VariableAccess(identifier) =>
      stack(identifier)
  }
}
