package de.adracus.elco.grammar.core

/**
 * Created by axel on 26/05/15.
 */
class ProductionList(productions: Production*) extends Iterable[Production] {
  override def toString = productions.mkString(" | ")

  def contains(producable: Producable) = productions.exists(_.contains(producable))

  override def iterator = productions.iterator
}