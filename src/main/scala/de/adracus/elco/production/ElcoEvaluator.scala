package de.adracus.elco.production

import de.adracus.elco.ast.AstNode
import de.adracus.elco.base.{BaseInstance, MethodInstance, Instance, Method}
import de.adracus.elco.evaluate.Evaluator

/**
 * Created by axel on 15/07/15.
 */
class ElcoEvaluator extends Evaluator[String] {
  def evaluate(node: AstNode) = node match {
    case ExpressionList(expressions) =>
      expressions.map(_.evaluate()).last

    case Constant(value) => de.adracus.elco.base.Integer.create(value)

    case FunctionDefinition(name, identifiers, body) =>
      val method = Method.createMethod(this.stack, body.evaluateFn, identifiers.identifiers)
      stack.const(name, method)
      method

    case Extraction(expression, name) =>
      expression.evaluate().asInstanceOf[BaseInstance](name)

    case ExpressionCall(expr, invokeList) =>
      val evaluated = expr.evaluate().asInstanceOf[BaseInstance]
      val option = evaluated.get("call")
      option match {
        case Some(MethodInstance(method)) => method(invokeList.expressions.map(_.evaluate()))

        case _ => throw new Exception("No method defined for apply")
      }


    case FunctionCall(name, invokeList) =>
      val inst = stack(name).asInstanceOf[BaseInstance]
      val option = inst.get("call")
      option match {
        case Some(MethodInstance(method)) => method(invokeList.expressions.map(_.evaluate()))

        case _ => throw new Exception("No method defined for apply")
      }

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
