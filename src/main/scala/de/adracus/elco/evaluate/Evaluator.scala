package de.adracus.elco.evaluate

import de.adracus.elco.ast.AstNode

/**
 * Created by axel on 15/07/15.
 */
trait Evaluator[A] {
  val stack = new Stack[A]()

  def evaluate(node: AstNode): Any

  implicit class EvaluatableNode(val astNode: AstNode) {
    def evaluate() = Evaluator.this.evaluate(astNode)

    def evaluateFn = evaluate _
  }
}
