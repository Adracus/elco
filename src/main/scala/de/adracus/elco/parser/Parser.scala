package de.adracus.elco.parser

import de.adracus.elco.grammar.core._
import de.adracus.elco.lexer.core.Token

import scala.collection.mutable

/**
 * Created by axel on 02/06/15.
 */
class Parser(val grammar: Grammar) {
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

  private def computeFirst() = {
    val first = new mutable.HashMap[String, Set[Statement]]().withDefaultValue(Set.empty)
    val rules = grammar.rules.groupBy(_.nonTerminal)

    def addToFirst(key: Statement, values: Statement*) = {
      first(key.name) = first(key.name) ++ values
    }


    for (nonTerminal <- grammar.statements
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

    addToFollow(grammar.startSymbol, End)
    val allRules = grammar.rules

    for (nonTerminal <- grammar.statements
         if nonTerminal.isInstanceOf[NonTerminal];
         rule <- allRules) {
      addToFollow(
        nonTerminal,
        rule.production.terminalsAfter(nonTerminal.asInstanceOf[NonTerminal]).toSeq:_*)
    }

    var old: mutable.Map[String, Set[Statement]] = null
    do {
      old = new mutable.HashMap[String, Set[Statement]]() ++ follow
      for (rule <- grammar.rules) {
        followStep(rule.production.toList, rule.nonTerminal)
      }
    } while (old != follow)

    Map[String, Set[Statement]]() ++ follow
  }

  def firstOf(rule: Rule) = firstStep(first, rule.toList)

  def ruleFor(nonTerminal: NonTerminal, token: Token) = {
    val possibleRules = grammar.rules.filter(_.nonTerminal == nonTerminal)
    possibleRules.find(firstOf(_).contains(Terminal(token.name))).get
  }

  def computeItemSets() = {
    val rules = grammar.rules.groupBy(_.nonTerminal).mapValues(Set() ++ _)

    val startingRule = grammar.rules.find(_.nonTerminal == grammar.startSymbol).get
    val itemSets = new mutable.HashSet() + ItemSet.buildSet(Set(Item.start(startingRule)), rules)

    var old: Set[ItemSet] = null
    do {
      old = Set() ++ itemSets
      val addition = mutable.HashSet[ItemSet]()

      for(itemSet <- itemSets) {
        addition ++= itemSet.advance(rules)
      }

      itemSets ++= addition
    } while (old != itemSets)

    itemSets
  }

  lazy val first = computeFirst()
  lazy val follow = computeFollow(first)
  lazy val itemSets = computeItemSets()
}
