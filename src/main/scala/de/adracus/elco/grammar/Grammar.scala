package de.adracus.elco.grammar

import scala.collection.mutable

/**
 * Created by axel on 26/05/15.
 */
class Grammar extends ProductionDSL {
  val rules = new mutable.LinkedHashSet[Rule]()
  private var startSymbol: Option[NonTerminal] = None
  private val precedences = new mutable.HashSet[Precedence]()
  private var count = 0

  def add(rule: Rule): Unit = {
    val nonTerminal = rule.nonTerminal
    require("Start" != nonTerminal.name, message = "Start is a reserved non-terminal name")
    if (startSymbol.isEmpty) startSymbol = Some(nonTerminal)
    rules += rule
  }

  implicit def symbolToRuleBuilder(symbol: Symbol): RuleBuilder = new RuleBuilder(this, symbol.name)

  class RuleBuilder(val grammar: Grammar, val name: String) {
    def build(production: ProductionList) = {
      val nonTerminal = NonTerminal(name)
      production.foreach(production => add(new Rule(nonTerminal, production)))
    }

    def := (string: String): Unit = :=(Production(List(Word(string)), None))
    def := (symbol: Symbol): Unit = :=(Production(List(NonTerminal(symbol.name)), None))
    def := (production: Production): Unit = build(new ProductionList(production))
    def := (productionList: ProductionList): Unit = build(productionList)
  }

  def build() = {
    if (startSymbol.isEmpty) throw new IllegalStateException("Start symbol has to be defined")
    new AugmentedGrammar(startSymbol.get, Set() ++ rules, precedences.toSet)
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

  override def toString = rules.mkString("\n")
}

object Grammar {
  def fromRules(rules: Seq[Rule]) = {
    val grammar = new Grammar()
    rules.foreach(grammar.add)
    grammar
  }
}
