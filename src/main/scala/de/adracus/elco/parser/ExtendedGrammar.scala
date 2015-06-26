package de.adracus.elco.parser

import de.adracus.elco.grammar._

import scala.Function.tupled

/**
 * Created by axel on 10/06/15.
 */
class ExtendedGrammar(grammar: AugmentedGrammar, itemSets: Set[ItemSet]) {
  private def computeRules(): Set[ExtendedRule] = {
    val itemsAtStart = itemSets.flatMap { itemSet =>
      itemSet.itemsAtStart.map((itemSet, _))
    }

    def advance(acc: List[ExtendedStatement], state: ItemSet, next: List[Producable]): List[ExtendedStatement] = next match {
      case head :: tail =>
        val nextState = state.advanceBy(head, grammar.rules)
        val transformed = head.extend(state, nextState)
        val newAcc = acc :+ transformed
        advance(newAcc, nextState, tail)

      case Nil => acc
    }

    itemsAtStart map tupled { (start, item) =>
      val rule = item.rule
      val nonTerminal = advance(List.empty, start, List(rule.nonTerminal)).head
      val production = ExtendedProduction(
        advance(List.empty, start, rule.toList).asInstanceOf[List[ExtendedProducable]])
      ExtendedRule(nonTerminal.asInstanceOf[ExtendedNonTerminal], production)
    }
  }

  def statements = rules.flatMap { rule =>
    Set(rule.nonTerminal) ++ rule.production.statements
  }

  val rules = computeRules()

  val startRule = rules.find(_.nonTerminal.name == "Start").get
  val startSymbol = startRule.nonTerminal
}
