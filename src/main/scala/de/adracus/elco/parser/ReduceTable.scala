package de.adracus.elco.parser

import de.adracus.elco.grammar.core.Terminal
import de.adracus.elco.grammar.core.Rule

import scala.collection.mutable

/**
 * Created by axel on 14/06/15.
 */
class ReduceTable(grammar: ExtendedGrammar, followSet: FollowSet) {
  private def mergedReductions() = {
    val tupledRules = grammar.rules.map { rule =>
      (rule, followSet(rule.nonTerminal))
    }

    def merge(tuples: Set[(ExtendedRule, Set[Terminal])]): (ItemSet, Rule, Set[Terminal]) = {
      val eRule = tuples.head._1
      val finalSet = eRule.production.lastSet
      val rule = eRule.rule
      val follows = tuples.flatMap(_._2)
      (finalSet, rule, follows)
    }

    def recurse(acc: Set[(ItemSet, Rule, Set[Terminal])], next: Set[(ExtendedRule, Set[Terminal])]): Set[(ItemSet, Rule, Set[Terminal])] = {
      if (next.isEmpty) acc
      else {
        val head = next.head
        val (rule, _) = head
        val same = next.filter {
          case (otherRule, _) =>
            otherRule.rule == rule.rule &&
            otherRule.production.lastSet == rule.production.lastSet
        }
        val merged = merge(same + head)
        recurse(acc + merged, next.tail)
      }
    }

    recurse(Set.empty, tupledRules)
  }

  private def tableTransform(tuples: Set[(ItemSet, Rule, Set[Terminal])]) = {
    val table = new mutable.HashMap[(ItemSet, Terminal), Rule]
    tuples.foreach {
      case (itemSet, rule, follows) =>
        follows.foreach { terminal =>
          if (rule.nonTerminal.name != "Start")
            table((itemSet, terminal)) = rule
        }
    }
    table.toMap
  }

  val table = tableTransform(mergedReductions())
}
