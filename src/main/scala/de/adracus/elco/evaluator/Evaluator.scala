package de.adracus.elco.evaluator

import de.adracus.elco.grammar.core._

import scala.collection.mutable

/**
 * Created by axel on 26/06/15.
 */
class Evaluator {
  private val evaluators = new mutable.HashSet[RuleEvaluator]()

  private def add(ruleEvaluator: RuleEvaluator) = evaluators += ruleEvaluator

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
}
