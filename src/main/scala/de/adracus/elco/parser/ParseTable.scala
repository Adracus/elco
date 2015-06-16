package de.adracus.elco.parser

import de.adracus.elco.grammar.core.{Terminal, Rule, Grammar}

/**
 * Created by axel on 15/06/15.
 */
class ParseTable(
                itemSets: List[ItemSet],
                startingRule: Rule,
                transitionTable: TransitionTable,
                reduceTable: ReduceTable
                  ) {
  val enumerator = new Enumerator[ItemSet]
  itemSets.foreach(enumerator(_)) // Enumerate the item sets in correct order

  val action = new ActionTable(transitionTable, itemSets, startingRule, enumerator, reduceTable)
  val goto = new GotoTable(transitionTable, enumerator)

  def expected(state: Int) = {
    action.table.collect {
      case ((otherState, statement), _) if otherState == state => statement
    }.toSet
  }
}

object ParseTable {
  def generate(grammar: Grammar) = {
    val sets = ItemSet.all(grammar)
    val eGrammar = new ExtendedGrammar(grammar, sets.toSet)
    val transitionTable = new TransitionTable(sets, grammar.rules)
    val first = new FirstSet(eGrammar)
    val follow = new FollowSet(eGrammar, first)
    val reduced = new ReduceTable(eGrammar, follow)

    new ParseTable(sets, grammar.startRule, transitionTable, reduced)
  }
}
