package de.adracus.elco.parser.core

import de.adracus.elco.grammar.core.NonTerminal

import scala.collection.mutable

/**
 * Created by axel on 15/06/15.
 */
class GotoTable(transitionTable: TransitionTable, enumerator: Enumerator[ItemSet]) {
  private def computeTable() = {
    val table = new mutable.HashMap[(Int, NonTerminal), Int]()

    transitionTable.transitions.foreach {
      case ((state, nt: NonTerminal), next) =>
        val startNo = enumerator(state)
        val endNo = enumerator(next)
        table((startNo, nt)) = endNo

      case _ =>
    }

    table.toMap
  }

  def apply(state: Int, nonTerminal: NonTerminal) = table((state, nonTerminal))

  val table = computeTable()
}
