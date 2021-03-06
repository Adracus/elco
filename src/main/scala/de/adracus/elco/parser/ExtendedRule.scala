package de.adracus.elco.parser

import de.adracus.elco.grammar.{ExtendedNonTerminal, ExtendedProducable, Rule}

/**
 * Created by axel on 10/06/15.
 */
case class ExtendedRule(
                         nonTerminal: ExtendedNonTerminal,
                         production: ExtendedProduction)
  extends Iterable[ExtendedProducable] {

  override def iterator = production.iterator

  override def toString() = s"$nonTerminal := $production"

  def rule = Rule(nonTerminal.base, production.production)
}
