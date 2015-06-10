package de.adracus.elco.grammar.core

/**
 * Created by axel on 30/05/15.
 */
case class Rule(nonTerminal: NonTerminal, production: Production) extends Iterable[Producable] {
  override def toString = s"$nonTerminal := $production"

  override def iterator: Iterator[Producable] = production.iterator

  def length = production.length
}
