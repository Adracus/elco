package de.adracus.elco.parser.core

/**
 * Created by axel on 26/05/15.
 */
class ProductionList(productions: Production*) {
  override def toString = productions.mkString(" | ")
}