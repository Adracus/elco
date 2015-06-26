package de.adracus.elco.parser

import de.adracus.elco.grammar.{AugmentedGrammar, Precedence, Rule}

/**
 * Created by axel on 15/06/15.
 */
class ParseTable(
    itemSets: List[ItemSet],
    startingRule: Rule,
    transitionTable: TransitionTable,
    reduceTable: ReduceTable,
    val precedendes: Set[Precedence]) {
  val enumerator = new Enumerator[ItemSet]
  itemSets.foreach(enumerator(_)) // Enumerate the item sets in correct order

  val action = new ActionTable(transitionTable, itemSets, startingRule, enumerator, reduceTable)
  val goto = new GotoTable(transitionTable, enumerator)

  def expected(state: Int) = {
    action.table.collect {
      case ((otherState, statement), _) if otherState == state => statement
    }.toSet
  }

  def precedenceOf(string: String) = precedendes.find(_.string == string)
}

object ParseTable {
  def generate(grammar: AugmentedGrammar) = {
    val sets = ItemSet.all(grammar)
    val eGrammar = new ExtendedGrammar(grammar, sets.toSet)
    val transitionTable = new TransitionTable(sets, grammar.rules)
    val first = new FirstSet(eGrammar)
    val follow = new FollowSet(eGrammar, first)
    val reduced = new ReduceTable(eGrammar, follow)

    new ParseTable(sets, grammar.startRule, transitionTable, reduced, grammar.precedences)
  }
}
