package de.adracus.elco.production.expressions

/**
 * Created by axel on 19/08/15.
 */
abstract class BaseCall(identifier: String, invokeList: InvokeList)
  extends Expression(identifier, invokeList)

case class FunctionCall(identifier: String, invokeList: InvokeList)
  extends BaseCall(identifier, invokeList)

case class ExpressionCall(expression: Expression, invokeList: InvokeList)
  extends BaseCall("call", invokeList)

object FunctionCall {
  def zeroArg(identifier: String) = FunctionCall(identifier, InvokeList.empty())
}