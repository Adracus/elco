package de.adracus.elco.parser.core

import de.adracus.elco.grammar.core._

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
