package de.adracus.elco.grammar.core

import scala.collection.mutable

/**
 * Created by axel on 26/05/15.
 */
class GrammarBuilder() {
  val rules = new mutable.LinkedHashSet[Rule]()
  private var startSymbol: Option[NonTerminal] = None
  private var precedences = new mutable.HashSet[Precedence]()
  private var count = 0

  def add(rule: Rule): Unit = {
    val nonTerminal = rule.nonTerminal
    require("Start" != nonTerminal.name, message = "Start is a reserved non-terminal name")
    if (startSymbol.isEmpty) startSymbol = Some(nonTerminal)
    rules += rule
  }

  implicit def symbolToBuilder(symbol: Symbol): RuleBuilder = new RuleBuilder(this, symbol.name)

  implicit def stringToProduction(string: String): Production = Production.terminal(string)

  implicit def statementToProduction(producable: Producable): Production = Production(List(producable))

  implicit def symbolToProduction(symbol: Symbol): Production = Production(List(NonTerminal(symbol.name)))

  class RuleBuilder(val grammar: GrammarBuilder, val name: String) {
    def build(productions: ProductionList) = {
      val nonTerminal = NonTerminal(name)
      productions.map(new Rule(nonTerminal, _)).foreach(add)
      nonTerminal
    }

    def := (productions: ProductionList): Unit = build(productions)
    def := (production: Production): Unit = build(new ProductionList(production))
  }

  def build() = {
    if (startSymbol.isEmpty) throw new IllegalStateException("Start symbol has to be defined")
    new Grammar(startSymbol.get, Set() ++ rules, precedences.toSet)
  }

  def precedence(pType: PrecedenceType, string: String) = {
    val prec = new Precedence(pType, string, count)
    if (!precedences.contains(prec)) {
      precedences.add(prec)
      count += 1
    }
  }

  def left(string: String) = precedence(Left, string)
  def right(string: String) = precedence(Right, string)
  def unary(string: String) = precedence(Unary, string)

  override def toString = rules.mkString("\n")
}

object GrammarBuilder {
  def fromRules(rules: Seq[Rule]) = {
    val grammar = new GrammarBuilder()
    rules.foreach(grammar.add)
    grammar
  }
}
