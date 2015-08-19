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
           |func main() {
           |${recurse(expr)}
            |}
        """.stripMargin

      case ExpressionList(expressions) =>
        expressions.map(recurse).mkString("\n")

      case Constant(value) => s"core.NewIntInstance($value)"

      case Extraction(expr, identifier) =>
        s"core.Get(${format(expr)}, $identifier)"

      case a: Assignment =>
        s"${a.identifier} := ${format(a.expression)}"

      case VariableAccess(name) => name

      case FunctionCall(name, params) =>
        s"core.Invoke($name, ${format(params)}}"

      case InvokeList(expressions) => expressions.map(format).mkString(", ")

      case default => ""//throw new Exception(s"Illegal $default")
    }


    format(astNode).lines.map("\t" * indent + _).mkString("\n")
  }
}
