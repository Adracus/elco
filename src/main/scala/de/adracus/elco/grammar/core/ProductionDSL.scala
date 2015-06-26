package de.adracus.elco.grammar.core

/**
 * Created by axel on 26/06/15.
 */
trait ProductionDSL {
  implicit def stringToProduction(string: String): Production = Production.terminal(string)

  implicit def statementToProduction(producable: Producable): Production = Production(List(producable))

  implicit def symbolToProduction(symbol: Symbol): Production = Production(List(NonTerminal(symbol.name)))
}
