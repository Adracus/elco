package de.adracus.elco.parser

import de.adracus.elco.grammar.core.Rule

/**
 * Created by axel on 04/06/15.
 */
case class ExtendedRule(nonTerminal: ExtendedNonTerminal, production: ExtendedProduction) {
  def toRule() = Rule(nonTerminal.statement, production.toProduction())

  def toERule() = Rule(nonTerminal.eStatement, production.toEProduction())

  def statementSet = production.statementSet + nonTerminal.statement

  def finalSet = nonTerminal.end
}
