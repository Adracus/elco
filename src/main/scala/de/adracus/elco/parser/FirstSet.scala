package de.adracus.elco.parser

import de.adracus.elco.grammar.core._

import scala.collection.mutable
/**
 * Created by axel on 11/06/15.
 */
class FirstSet(grammar: ExtendedGrammar) {
  private def firstStep(first: Map[ExtendedNonTerminal, Set[Terminal]], statements: List[ExtendedProducable]) = {
    def inner(acc: Set[Terminal], statements: List[ExtendedStatement]): Set[Terminal] = statements match {
      case Nil => acc + Epsilon
      case st :: tail =>
        if (st.isInstanceOf[ExtendedTerminal]) acc + st.base.asInstanceOf[Terminal]
        else {
          val nt = st.asInstanceOf[ExtendedNonTerminal]
          if (first(nt) contains Epsilon) inner(acc ++ (first(nt) - Epsilon), tail)
          else acc ++ first(nt)
        }
    }

    inner(Set.empty, statements)
  }

  private def computeFirst(grammar: ExtendedGrammar) = {
    val first = new mutable.HashMap[ExtendedNonTerminal, Set[Terminal]]().withDefaultValue(Set.empty)
    val rules = grammar.rules.groupBy(_.nonTerminal)

    def addToFirst(key: ExtendedNonTerminal, values: Terminal*) = {
      first(key) = first(key) ++ values
    }

    val nonTerminals = grammar.rules.map(_.nonTerminal)

    for (nonTerminal <- nonTerminals;
         rule <- rules(nonTerminal)) {
      if (rule.production.isEpsilonProduction) {
        addToFirst(nonTerminal, Epsilon)
      }
      if (rule.production.startsWithTerminal) {
        addToFirst(nonTerminal, rule.head.base.asInstanceOf[Terminal])
      }
    }

    var old: mutable.Map[ExtendedNonTerminal, Set[Terminal]] = null
    do {
      old = new mutable.HashMap[ExtendedNonTerminal, Set[Terminal]]() ++ first

      for (ruleSet <- rules.values; rule <- ruleSet) {
        val A = rule.nonTerminal
        val production = rule.production

        addToFirst(A, firstStep(first.toMap.withDefaultValue(Set.empty), production.toList).toSeq:_*)
      }
    } while (old != first)

    first.toMap
  }

  val table = computeFirst(grammar)
}

