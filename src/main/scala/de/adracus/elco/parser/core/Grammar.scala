package de.adracus.elco.parser.core

import scala.collection.mutable

/**
 * Created by axel on 26/05/15.
 */
class Grammar {
  private val symbols = new mutable.HashMap[String, NonTerminal]()

  def add(name: String, nonTerminal: NonTerminal) = {
    symbols(name) = nonTerminal
  }

  implicit def symbolToBuilder(symbol: Symbol): NonTerminalBuilder = new NonTerminalBuilder(this, symbol.name)

  implicit def stringToProduction(string: String): Production = Production.terminal(string)

  implicit def statementToProduction(statement: Statement): Production = Production(statement)

  implicit def productionToList(production: Production): ProductionList = new ProductionList(production)

  implicit def symbolToProduction(symbol: Symbol): Production = {
    val name = symbol.name
    val nonTerminal = symbols.get(name)
    Production(if (nonTerminal.isEmpty) Reference(name) else nonTerminal.get)
  }


  class NonTerminalBuilder(val grammar: Grammar, val name: String) {
    def build(productions: ProductionList) = {
      val nonTerminal = new NonTerminal(name, productions)
      symbols(name) = nonTerminal
      nonTerminal
    }

    def := (productions: ProductionList) = build(productions)
  }

  override def toString = symbols.values.mkString("\n")
}
