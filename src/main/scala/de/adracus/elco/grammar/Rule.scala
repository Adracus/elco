package de.adracus.elco.grammar

/**
 * Created by axel on 30/05/15.
 */
case class Rule(nonTerminal: NonTerminal, production: Production) extends Iterable[Producable] {

  override def toString = s"$nonTerminal := $production"

  def iterator: Iterator[Producable] = production.iterator

  def length = production.length

  def evaluation = production.evaluation

  def +(producable: Producable) = Rule(nonTerminal, production & producable)
}
