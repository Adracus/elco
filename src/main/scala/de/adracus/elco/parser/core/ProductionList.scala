package de.adracus.elco.parser.core

/**
 * Created by axel on 26/05/15.
 */
class ProductionList(productions: Production*) extends Iterable[Production] {
  override def toString = productions.mkString(" | ")

  def contains(statement: Statement) = productions.exists(_.contains(statement))

  override def iterator: Iterator[Production] = productions.iterator
}