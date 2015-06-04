package de.adracus.elco.parser

import de.adracus.elco.grammar.core.{Rule, NonTerminal}

/**
 * Created by axel on 04/06/15.
 */
case class ExtendedRule(nonTerminal: ExtendedNonTerminal, production: ExtendedProduction) {
  def toRule = Rule(nonTerminal.statement, production.toProduction())

  def statementSet = production.statementSet + nonTerminal.statement
}
