package de.adracus.elco.parser

import de.adracus.elco.grammar.core._

sealed trait ExtendedStatement {
  val start: ItemSet
  val end: BaseItemSet
  def statement: Statement
  def eStatement: Statement
  def name = statement.name
}

case class ExtendedTerminal(start: ItemSet, terminal: BaseTerminal, end: BaseItemSet)
  extends ExtendedStatement {
  def statement = terminal
  def eStatement =
    if (terminal.isInstanceOf[Terminal])
      Terminal(start.hashCode() + " " + terminal.name + " " + end.hashCode())
    else terminal
}

case class ExtendedNonTerminal(start: ItemSet, nonTerminal: NonTerminal, end: BaseItemSet)
  extends ExtendedStatement {
  def statement = nonTerminal
  def eStatement = NonTerminal(start.hashCode() + " " + nonTerminal.name + " " + end.hashCode())
}

object ExtendedStatement {
  def extend(start: ItemSet, statement: Statement, end: BaseItemSet): ExtendedStatement = statement match {
    case t: BaseTerminal => ExtendedTerminal(start, t, end)
    case n: NonTerminal => ExtendedNonTerminal(start, n, end)
  }

  def apply(start: ItemSet, statement: Statement, end: BaseItemSet) = extend(start, statement, end)
}

object ExtendedEpsilon extends ExtendedTerminal(ItemSet.empty, Epsilon, ItemSet.empty)

object ExtendedEnd extends ExtendedTerminal(ItemSet.empty, End, FinalSet)
