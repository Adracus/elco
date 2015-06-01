package de.adracus.elco.parser.core

/**
 * Created by axel on 30/05/15.
 */
case class Rule(nonTerminal: NonTerminal, production: Production) extends Iterable[Statement] {
  override def toString = s"$nonTerminal := $production"

  override def iterator: Iterator[Statement] = production.iterator

  def length = production.length
}
