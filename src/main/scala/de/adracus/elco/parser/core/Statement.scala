package de.adracus.elco.parser.core

/**
 * Created by axel on 26/05/15.
 */
sealed trait Statement {
  val name: String

  override def toString = name
}

sealed trait BaseTerminal extends Statement

case class Terminal(name: String) extends BaseTerminal {
  def t = this
}

object Epsilon extends BaseTerminal {
  override val name = "Epsilon"
}

sealed trait BaseNonTerminal extends Statement

case class Reference(name: String) extends BaseNonTerminal {
  def this(symbol: Symbol) = this(symbol.name)
}

case class NonTerminal(name: String, productions: ProductionList) extends BaseNonTerminal {
  override def toString = s"$name := $productions"
}
