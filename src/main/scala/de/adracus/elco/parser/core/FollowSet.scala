package de.adracus.elco.parser.core

import de.adracus.elco.grammar.core._

import scala.collection.mutable

/**
 * Created by axel on 11/06/15.
 */
class FollowSet(grammar: ExtendedGrammar, firstSet: FirstSet) {
  private def firstStep(firstSet: FirstSet, statements: List[ExtendedStatement]) = {
    val first = firstSet.table
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

  private def computeFollow(grammar: ExtendedGrammar, firstSet: FirstSet) = {
    val follow = new mutable.HashMap[ExtendedNonTerminal, Set[Terminal]]().withDefaultValue(Set.empty)

    def addToFollow(key: ExtendedNonTerminal, values: Terminal*) = {
      follow(key) = follow(key) ++ values
    }

    def followStep(statements: List[ExtendedStatement], producer: ExtendedNonTerminal) = {
      def inner(statements: List[ExtendedStatement]): Unit = statements match {
        case Nil =>
        case (st: ExtendedNonTerminal) :: Nil =>
          addToFollow(st, follow(producer).toSeq:_*)
        case st :: tail =>
          if (!st.isInstanceOf[ExtendedNonTerminal]) inner(tail)
          else {
            val nt = st.asInstanceOf[ExtendedNonTerminal]
            val addition = firstStep(firstSet, tail)
            addToFollow(nt, (addition - Epsilon).toSeq:_*)
            if (addition contains Epsilon) {
              addToFollow(nt, follow(producer).toSeq:_*)
            }
            inner(tail)
          }
        case _ =>
      }

      inner(statements)
    }

    addToFollow(grammar.startSymbol, End)
    val allRules = grammar.rules
    val nonTerminals = grammar.statements.collect {
      case eNt: ExtendedNonTerminal => eNt
    }

    for (nonTerminal <- nonTerminals; rule <- allRules) {
      addToFollow(
        nonTerminal,
        rule.production.terminalsAfter(nonTerminal).map(_.base.asInstanceOf[Terminal]).toSeq:_*)
    }

    var old: mutable.Map[ExtendedNonTerminal, Set[Terminal]] = null
    do {
      old = new mutable.HashMap[ExtendedNonTerminal, Set[Terminal]]() ++ follow
      for (rule <- grammar.rules) {
        followStep(rule.production.toList, rule.nonTerminal)
      }
    } while (old != follow)

    follow.toMap
  }

  val table = computeFollow(grammar, firstSet)

  def apply(extendedNonTerminal: ExtendedNonTerminal) = table(extendedNonTerminal)
}
