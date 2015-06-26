package de.adracus.elco.evaluator

import de.adracus.elco.grammar.{NonTerminal, Rule, Production}
import de.adracus.elco.grammar.core._
import de.adracus.elco.parser.{Node, Leaf, Tree}

import scala.collection.mutable

/**
 * Created by axel on 26/06/15.
 */
class Evaluator {
  private val evaluators = new mutable.HashMap[Rule, RuleEvaluator]()

  private def add(ruleEvaluator: RuleEvaluator) = evaluators(ruleEvaluator.rule) = ruleEvaluator

  private class RuleEvaluatorBuilder {
    def apply(symbol: Symbol) = {
      val nonTerminal = NonTerminal(symbol.name)
      new StartingRule(nonTerminal)
    }

    def rule = this

    private class StartingRule(val nonTerminal: NonTerminal) {
      def :=(production: Production) = Rule(nonTerminal, production)

      private class StartingEvaluator(val rule: Rule) {
        def evaluate(evaluation: Seq[Any] => Any) = {
          val evaluator = new RuleEvaluator(rule, evaluation)
          add(evaluator)
        }
      }
    }
  }

  val on = new RuleEvaluatorBuilder

  import de.adracus.elco.parser.core._
  def evaluate(tree: Tree): Any = tree match {
    case leaf: Leaf =>
      leaf.token.value.getOrElse(Unit)

    case n: Node =>
      val subEvaluations = n.children.map(evaluate)
      val entry = evaluators.get(n.rule)
      if (entry.isDefined) entry.get.evaluation(subEvaluations)
      else Unit
  }
}
