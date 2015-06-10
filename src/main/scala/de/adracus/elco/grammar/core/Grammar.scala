package de.adracus.elco.grammar.core

/**
 * Created by axel on 10/06/15.
 */
class Grammar(pointer: NonTerminal, ruleSet: Set[Rule]) {
  require(ruleSet.exists(_.nonTerminal == pointer), message = "Pointer has to be contained in rule set")

  val startSymbol = NonTerminal("Start")
  val startRule = Rule(startSymbol, Production(Seq(pointer)))
  val rules = ruleSet + startRule
}
