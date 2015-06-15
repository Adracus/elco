package de.adracus.elco.parser

import de.adracus.elco.grammar.core.{End, Rule, Statement}

import scala.collection.mutable

/**
 * Created by axel on 14/06/15.
 */
class ActionTable(
                   transitionTable: TransitionTable,
                   itemSets: List[ItemSet],
                   startingRule: Rule,
                   enumerator: Enumerator[ItemSet],
                   reduceTable: ReduceTable) {

  def apply(state: Int, statement: Statement) = table((state, statement))

  private def computeTable() = {
    val table = new mutable.HashMap[(Int, Statement), Action]()
    itemSets.filter(hasEndRule).foreach { itemSet =>
      val number = enumerator(itemSet)
      table((number, End)) = Accept
    }

    transitionTable.transitions.foreach {
      case ((state, producable), next) =>
        val startNo = enumerator(state)
        val endNo = enumerator(next)
        table((startNo, producable)) = Shift(endNo)
    }

    reduceTable.table.foreach {
      case ((state, terminal), rule) =>
        val startNo = enumerator(state)
        table((startNo, terminal)) = Reduce(rule)
    }

    table.toMap
  }

  private def hasEndRule(itemSet: ItemSet) = {
    itemSet.items.exists { item =>
      item.isAtEnd && item.rule == startingRule
    }
  }

  val table = computeTable()
}
