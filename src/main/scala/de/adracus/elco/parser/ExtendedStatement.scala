package de.adracus.elco.parser

import de.adracus.elco.grammar.core.{BaseTerminal, NonTerminal, Terminal, Statement}

sealed trait ExtendedStatement {
  val start: ItemSet
  val end: BaseItemSet
  def statement: Statement
}

case class ExtendedTerminal(start: ItemSet, terminal: BaseTerminal, end: BaseItemSet)
  extends ExtendedStatement {
  def statement = terminal
}

case class ExtendedNonTerminal(start: ItemSet, nonTerminal: NonTerminal, end: BaseItemSet)
  extends ExtendedStatement {
  def statement = nonTerminal
}

object ExtendedStatement {
  def extend(start: ItemSet, statement: Statement, end: BaseItemSet): ExtendedStatement = statement match {
    case t: BaseTerminal => ExtendedTerminal(start, t, end)
    case n: NonTerminal => ExtendedNonTerminal(start, n, end)
  }

  def apply(start: ItemSet, statement: Statement, end: BaseItemSet) = extend(start, statement, end)
}
