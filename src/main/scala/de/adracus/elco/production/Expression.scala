package de.adracus.elco.production

import de.adracus.elco.ast.AstNode
import de.adracus.elco.base.IntegerInstance

/**
 * Created by axel on 08/07/15.
 */
abstract class Expression(input: Any*) extends AstNode(input:_*)

case class ExpressionList(expressions: List[Expression]) extends AstNode(expressions:_*) {
  def :+(expression: Expression) = ExpressionList(expressions :+ expression)
}

object ExpressionList {
  def single(expression: Expression) = ExpressionList(List(expression))
}

case class Constant(value: Int) extends Expression(value)

case class VariableAccess(identifier: String) extends Expression(identifier)

case class IdentifierList(identifiers: List[String]) extends Expression(identifiers:_*) {
  def :+(identifier: String) = IdentifierList(identifiers :+ identifier)
}

object IdentifierList {
  def empty() = IdentifierList(List.empty)
}

abstract class BaseCall(identifier: String, invokeList: InvokeList)
  extends Expression(identifier, invokeList)

case class FunctionCall(identifier: String, invokeList: InvokeList)
  extends BaseCall(identifier, invokeList)

case class ExpressionCall(expression: Expression, invokeList: InvokeList)
  extends BaseCall("call", invokeList)

object FunctionCall {
  def zeroArg(identifier: String) = FunctionCall(identifier, InvokeList.empty())
}

case class FunctionDefinition(identifier: String, identifiers: IdentifierList, body: ExpressionList)
  extends Expression(identifier, identifiers, body)

case class Extraction(expression: Expression, identifier: String)
  extends Expression(expression, identifier)

abstract class Assignment(
    identifier: String,
    expr: Expression)
  extends Expression(identifier, expr) {
  def expression: Expression
}

case class ValAssignment(identifier: String, expression: Expression)
  extends Assignment(identifier, expression)

case class VarAssignment(identifier: String, expression: Expression)
  extends Assignment(identifier, expression)

case class Conditional(condition: Expression, ifBody: Expression, elseBody: Option[Expression])
  extends Expression(condition, ifBody, elseBody)

case class InvokeList(expressions: List[Expression]) extends Expression(expressions:_*) {
  def :+(expression: Expression) = InvokeList(expressions :+ expression)
}

object InvokeList {
  def single(expression: Expression) = InvokeList(List(expression))
  def empty() = InvokeList(List())
}

object Unit extends Expression
