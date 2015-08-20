package de.adracus.elco.production

import de.adracus.elco.ast.AstNode
import de.adracus.elco.production.expressions._

/**
 * Created by axel on 18/08/15.
 */
object CodeGenerator {
  def generate(astNode: AstNode): String = generate(astNode, 0)

  def generate(astNode: AstNode, indent: Int): String = {
    def recurse(node: AstNode) = generate(node, indent + 1)

    def format(node: AstNode): String = node match {
      case ElcoFile(expr) =>
        s"""package main
           |
           |import "elco/core"
           |
           |func main() BaseInstance {
           |${recurse(expr)}
            |}
        """.stripMargin

      case ExpressionList(expressions) =>
        expressions.map(recurse).mkString("\n")

      case Constant(value) => s"core.NewIntInstance($value)"

      case Extraction(expr, identifier) =>
        s"""core.Get(${format(expr)}, "$identifier")"""

      case a: Assignment =>
        s"""core.Scope.Set("${a.identifier}", ${format(a.expression)})"""

      case VariableAccess(name) => s"""core.Scope.Get("$name")"""

      case FunctionCall(name, params) =>
        s"""core.Invoke(core.Scope.Get("$name"), ${format(params)})"""

      case Conditional(condition, ifBody, elseOption) =>
        val s = s"""if core.AsBool(${format(condition)}) {
           |${generate(ifBody)}
           |}
         """.stripMargin

        elseOption match {
          case Some(elseBody) => s +
            s"""
              |else {
              |${generate(elseBody)}
              |}
            """.stripMargin
          case None => s
        }

      case InvokeList(expressions) => expressions.map(format).mkString(", ")

      case ExpressionCall(expression, invokeList) => s"""core.Invoke(${format(expression)}, ${format(invokeList)})"""

      case default => throw new Exception(s"Illegal $default")
    }


    format(astNode).lines.map("\t" * indent + _).mkString("\n")
  }
}
