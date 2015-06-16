package de.adracus.elco.parser.core

import de.adracus.elco.grammar.core._

/**
 * Created by axel on 11/06/15.
 */
case class ExtendedProduction(statements: List[ExtendedProducable]) extends Iterable[ExtendedProducable] {
  require(
    statements.nonEmpty,
    "A production cannot be empty")

  require(
    statements.filter(_.isInstanceOf[ExtendedEpsilon]).lengthCompare(1) <= 0,
    "Epsilon cannot be contained multiple times")

  def isEpsilonProduction = statements.forall(_.isInstanceOf[ExtendedEpsilon])

  override def iterator = statements.iterator

  def startsWithTerminal = {
    val headOption = statements.headOption
    if (headOption.isEmpty) false
    else headOption.get.isInstanceOf[ExtendedTerminal]
  }

  def terminalsAfter(nonTerminal: ExtendedNonTerminal) = {
    def inner(acc: Set[ExtendedTerminal], remaining: List[ExtendedStatement]): Set[ExtendedTerminal] = remaining match {
      case a :: next :: tail =>
        if (a == nonTerminal && next.isInstanceOf[ExtendedTerminal]) inner(acc + next.asInstanceOf[ExtendedTerminal], tail)
        else inner(acc, next +: tail)
      case _ => acc
    }

    inner(Set.empty, statements.toList)
  }

  override def toString() = statements.mkString(" ")

  def production = Production(statements.map(_.base).asInstanceOf[List[Producable]])

  def lastSet = statements.last.end
}
