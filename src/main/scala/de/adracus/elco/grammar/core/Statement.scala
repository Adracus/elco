package de.adracus.elco.grammar.core

sealed trait Statement {
  val name: String

  override def toString = name
}

sealed trait Terminal extends Statement

sealed trait Producable extends Statement

sealed case class Word(name: String) extends Terminal with Producable

object Epsilon extends Terminal with Producable {
  override val name = "Epsilon"
}

object End extends Terminal {
  override val name = "$"
}

case class NonTerminal(name: String) extends Statement with Producable {
  def this(symbol: Symbol) = this(symbol.name)
}