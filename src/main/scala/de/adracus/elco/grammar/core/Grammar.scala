package de.adracus.elco.grammar.core

import de.adracus.elco.lexer.core.{Token, TokenStream}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * Created by axel on 26/05/15.
 */
class Grammar {
  protected val rules = new mutable.HashMap[NonTerminal, Set[Rule]]()
  protected val statements = new mutable.LinkedHashMap[String, Statement]()

  lazy val first = computeFirst()
  lazy val follow = computeFollow(first)

  def add(rule: Rule): Unit = {
    val nonTerminal = rule.nonTerminal
    addStatement(nonTerminal)
    rule.production.foreach(addStatement)
    rules(nonTerminal) = rules.getOrElse(nonTerminal, default = Set.empty) + rule
  }

  def addStatement(statement: Statement): Unit = {
    val entry = statements.get(statement.name)
    if (entry.isDefined) {
      require(entry.get == statement, message = s"Statement '${statement.name}' cannot be defined twice")
    }
    statements(statement.name) = statement
  }

  def startSymbol = statements.head._2

  implicit def symbolToBuilder(symbol: Symbol): RuleBuilder = new RuleBuilder(this, symbol.name)

  implicit def stringToProduction(string: String): Production = Production.terminal(string)

  implicit def statementToProduction(statement: Statement): Production = Production(statement)

  implicit def productionToList(production: Production): ProductionList = new ProductionList(production)

  implicit def symbolToProduction(symbol: Symbol): Production = Production(NonTerminal(symbol.name))

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

  private def firstStep(first: Map[String, Set[Statement]], statements: List[Statement]) = {
    def inner(acc: Set[Statement], statements: List[Statement]): Set[Statement] = statements match {
      case Nil => acc + Epsilon
      case st :: tail =>
        if (st.isInstanceOf[BaseTerminal]) acc + st
        else {
          if (first(st.name) contains Epsilon) inner(acc ++ (first(st.name) - Epsilon), tail)
          else acc ++ first(st.name)
        }
    }

    inner(Set.empty, statements)
  }

  override def toString = rules.mkString("\n")

  private def computeFirst() = {
    val first = new mutable.HashMap[String, Set[Statement]]().withDefaultValue(Set.empty)

    def addToFirst(key: Statement, values: Statement*) = {
      first(key.name) = first(key.name) ++ values
    }


    for (nonTerminal <- statements.values
         if nonTerminal.isInstanceOf[NonTerminal];
         rule <- rules(nonTerminal.asInstanceOf[NonTerminal])) {
      if (rule.production.isEpsilonProduction) {
        addToFirst(nonTerminal, Epsilon)
      }
      if (rule.production.startsWithTerminal) {
        addToFirst(nonTerminal, rule.head)
      }
    }

    var old: mutable.Map[String, Set[Statement]] = null
    do {
      old = new mutable.HashMap[String, Set[Statement]]() ++ first

      for (ruleSet <- rules.values; rule <- ruleSet) {
        val A = rule.nonTerminal
        val production = rule.production

        addToFirst(A, firstStep(first.toMap.withDefaultValue(Set.empty), production.toList).toSeq:_*)
      }
    } while (old != first)

    Map[String, Set[Statement]]() ++ first
  }

  private def computeFollow(first: Map[String, Set[Statement]]) = {
    val follow = new mutable.HashMap[String, Set[Statement]]().withDefaultValue(Set.empty)

    def addToFollow(key: Statement, values: Statement*) = {
      follow(key.name) = follow(key.name) ++ values
    }

    def followStep(statements: List[Statement], producer: NonTerminal) = {
      def inner(statements: List[Statement]): Unit = statements match {
        case Nil =>
        case st :: Nil =>
          if (st.isInstanceOf[NonTerminal]) addToFollow(st, follow(producer.name).toSeq:_*)
        case st :: tail =>
          if (!st.isInstanceOf[NonTerminal]) inner(tail)
          else {
            val addition = firstStep(first, tail)
            addToFollow(st, (addition - Epsilon).toSeq:_*)
            if (addition contains Epsilon) {
              addToFollow(st, follow(producer.name).toSeq:_*)
            }
            inner(tail)
          }
        case _ =>
      }

      inner(statements)
    }

    addToFollow(startSymbol, End)
    val allRules = rules.values.flatten

    for (nonTerminal <- statements.values
         if nonTerminal.isInstanceOf[NonTerminal];
         rule <- allRules) {
      addToFollow(
        nonTerminal,
        rule.production.terminalsAfter(nonTerminal.asInstanceOf[NonTerminal]).toSeq:_*)
    }

    var old: mutable.Map[String, Set[Statement]] = null
    do {
      old = new mutable.HashMap[String, Set[Statement]]() ++ follow
      for (ruleSet <- rules.values; rule <- ruleSet) {
        followStep(rule.production.toList, rule.nonTerminal)
      }
    } while (old != follow)

    Map[String, Set[Statement]]() ++ follow
  }

  def firstOf(rule: Rule) = firstStep(first, rule.toList)

  def ruleFor(nonTerminal: NonTerminal, token: Token) = {
    val possibleRules = rules(nonTerminal)
    possibleRules.find(firstOf(_).contains(Terminal(token.name))).get
  }

  def parse(tokenStream: TokenStream) = {
    val stack = new mutable.Stack[Statement]()
    stack.push(End, startSymbol)

    while (!stack.isEmpty) {
      val token = tokenStream.lookahead
      val top = stack.pop()
      top match {
        case Terminal(name) =>
          if (token.name == name) tokenStream.consume()
          else throw Unexpected(Terminal(name), token)
        case nt: NonTerminal =>
          val rule = ruleFor(nt, token)
          stack.pushAll(rule.toSeq.reverse)
        case End =>
          if ("EOF" == token.name) tokenStream.consume()
          else throw Unexpected(End, token)
        case _ => Unexpected(Epsilon, token)
      }
    }

    println("Valid sequence")
  }

  case class Unexpected(expected: Statement, actual: Token) extends Exception {
    override def toString = s"Expected '$expected' but got '$actual'"
  }
}
