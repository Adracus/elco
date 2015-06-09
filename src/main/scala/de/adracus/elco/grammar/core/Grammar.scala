package de.adracus.elco.grammar.core

import de.adracus.elco.lexer.core.Token

import scala.collection.mutable

/**
 * Created by axel on 26/05/15.
 */
class Grammar() {
  val rules = new mutable.HashSet[Rule]()
  protected val _statements = new mutable.LinkedHashMap[String, Producable]()

  def add(rule: Rule): Unit = {
    val nonTerminal = rule.nonTerminal
    addStatement(nonTerminal)
    rule.production.foreach(addStatement)
    rules += rule
  }

  def addStatement(statement: Producable): Unit = {
    val entry = _statements.get(statement.name)
    if (entry.isDefined) {
      require(entry.get == statement, message = s"Statement '${statement.name}' cannot be defined twice")
    }
    _statements(statement.name) = statement
  }

  def statements = _statements.values

  def startSymbol = _statements.head._2.asInstanceOf[NonTerminal]

  implicit def symbolToBuilder(symbol: Symbol): RuleBuilder = new RuleBuilder(this, symbol.name)

  implicit def stringToProduction(string: String): Production = Production.terminal(string)

  implicit def statementToProduction(producable: Producable): Production = Production(Seq(producable))

  implicit def productionToList(production: Production): ProductionList = new ProductionList(production)

  implicit def symbolToProduction(symbol: Symbol): Production = Production(Seq(NonTerminal(symbol.name)))

  implicit def symbolToProductionList(symbol: Symbol): ProductionList = new ProductionList(symbolToProduction(symbol))

  implicit def stringToProductionList(string: String): ProductionList = new ProductionList(stringToProduction(string))

  class RuleBuilder(val grammar: Grammar, val name: String) {
    def build(productions: ProductionList) = {
      val nonTerminal = NonTerminal(name)
      productions.map(new Rule(nonTerminal, _)).foreach(add)
      nonTerminal
    }

    def := (productions: ProductionList) = build(productions)
  }

  override def toString = rules.mkString("\n")

  case class Unexpected(expected: Statement, actual: Token) extends Exception {
    override def toString = s"Expected '$expected' but got '$actual'"
  }
}

object Grammar {
  def fromRules(rules: Seq[Rule]) = {
    val grammar = new Grammar()
    rules.foreach(grammar.add)
    grammar
  }
}
