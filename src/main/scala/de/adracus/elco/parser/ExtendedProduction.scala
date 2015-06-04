package de.adracus.elco.parser

import de.adracus.elco.grammar.core.Production

/**
 * Created by axel on 04/06/15.
 */
case class ExtendedProduction(statements: ExtendedStatement*) {
  def toProduction() = Production(statements.map(_.statement):_*)

  def toEProduction() = Production(statements.map(_.eStatement):_*)

  def statementSet = statements.map(_.statement).toSet
}
