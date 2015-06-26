package de.adracus.elco.evaluator

import de.adracus.elco.grammar._
import de.adracus.elco.parser.{Leaf, Node, Tree}

import scala.collection.mutable

/**
 * Created by axel on 26/06/15.
 */
class Evaluator extends ProductionDSL {
  private val evaluators = new mutable.HashMap[Rule, RuleEvaluator]()

  private def add(ruleEvaluator: RuleEvaluator) = evaluators(ruleEvaluator.rule) = ruleEvaluator

  protected class RuleBuilder(val nonTerminal: NonTerminal) {
    def :=(production: Production) = Rule(nonTerminal, production)
  }

  implicit def symbolToRuleBuilder(symbol: Symbol): RuleBuilder = new RuleBuilder(NonTerminal(symbol.name))

  implicit def ruleToEvaluationBuilder(rule: Rule): EvaluatorBuilder = new EvaluatorBuilder(rule)

  protected class EvaluatorBuilder(val rule: Rule) {
    def evaluate(evaluation: Seq[Any] => Any): Unit = {
      add(new RuleEvaluator(rule, evaluation))
    }

    def apply(evaluation: Seq[Any] => Any) = evaluate(evaluation)
  }

  def eval(tree: Tree): Any = tree match {
    case leaf: Leaf =>
      leaf.token.value.getOrElse(Unit)

    case n: Node =>
      val subEvaluations = n.children.map(eval)
      evaluators.get(n.rule)
        .map(_.evaluation(subEvaluations))
        .getOrElse(Unit)
  }
}
