package de.adracus.elco.grammar

/**
 * Created by axel on 26/06/15.
 */
trait ProductionDSL {
  implicit def stringToTerminal(string: String): Producable = Word(string)

  implicit def stringToBuilder(string: String): ProductionBuilder =
    new ProductionBuilder(string)

  implicit def statementToBuilder(statement: Producable): ProductionBuilder =
    new ProductionBuilder(statement)

  implicit def symbolToNonTerminal(symbol: Symbol): NonTerminal = NonTerminal(symbol.name)

  implicit def symbolToBuilder(symbol: Symbol): ProductionBuilder = new ProductionBuilder(symbol)
}
