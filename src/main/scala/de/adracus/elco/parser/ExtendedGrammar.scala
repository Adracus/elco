package de.adracus.elco.parser

import de.adracus.elco.grammar.core.{Grammar, NonTerminal}

/**
 * Created by axel on 04/06/15.
 */
class ExtendedGrammar(val startSymbol: NonTerminal, val rules: Set[ExtendedRule]) {
  def toGrammar() = {
    Grammar.fromRules(rules.map(_.toRule).toSeq)
  }

  def toEGrammar() = {
    Grammar.fromRules(rules.map(_.toERule()).toSeq)
  }

  def startingRule = rules.find { rule =>
    rule.nonTerminal.statement == startSymbol && rule.nonTerminal.end.asInstanceOf[ItemSet].isEmpty
  }.get

  def statements = rules.flatMap(_.production.statements)

  override def toString() = rules.mkString("\n")
}
