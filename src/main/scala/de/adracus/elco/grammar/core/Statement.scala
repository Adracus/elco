package de.adracus.elco.grammar.core

/**
 * Created by axel on 26/05/15.
 */
sealed trait Statement {
  val name: String

  override def toString = name
}

sealed trait BaseTerminal extends Statement

case class Terminal(name: String) extends BaseTerminal

object Epsilon extends BaseTerminal {
  override val name = "Epsilon"
}

object End extends BaseTerminal {
  override val name = "$"
}

case class NonTerminal(name: String) extends Statement {
  def this(symbol: Symbol) = this(symbol.name)
}