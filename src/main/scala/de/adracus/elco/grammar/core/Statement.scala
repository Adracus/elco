package de.adracus.elco.grammar.core

import de.adracus.elco.parser.core.ItemSet

// Base classes

sealed trait BaseStatement {
  val name: String

  override def toString = name
}

sealed trait BaseProducable extends BaseStatement

sealed trait BaseTerminal extends BaseStatement

sealed trait BaseNonTerminal extends BaseProducable

sealed trait BaseEpsilon extends BaseTerminal with BaseProducable {
  override val name = "Epsilon"
}

sealed trait BaseEnd extends BaseTerminal {
  override val name = "$"
}

// Standard classes

sealed trait Statement extends BaseStatement {
  def extend(start: ItemSet, end: ItemSet): ExtendedStatement
}

sealed trait Producable extends Statement with BaseProducable

sealed trait Terminal extends Statement with BaseTerminal

trait EndSymbol extends Terminal with BaseEnd

object End extends EndSymbol {
  def extend(start: ItemSet, end: ItemSet) = ExtendedEnd(start, end)
}

trait EpsilonSymbol extends Terminal with Producable with BaseEpsilon

object Epsilon extends EpsilonSymbol {
  def extend(start: ItemSet, end: ItemSet) = ExtendedEpsilon(start, end)
}

case class NonTerminal(name: String) extends Producable with BaseNonTerminal {
  def this(symbol: Symbol) = this(symbol.name)

  def extend(start: ItemSet, end: ItemSet) = ExtendedNonTerminal(start, name, end)
}

sealed case class Word(name: String) extends Terminal with Producable {
  def extend(start: ItemSet, end: ItemSet) = ExtendedWord(start, name, end)
}

// Extended classes

sealed trait ExtendedStatement extends BaseStatement {
  def start: ItemSet
  def end: ItemSet
  def base: Statement
}

sealed trait ExtendedProducable extends ExtendedStatement with BaseProducable

sealed trait ExtendedTerminal extends ExtendedStatement with BaseTerminal

sealed case class ExtendedEpsilon(start: ItemSet, end: ItemSet)
  extends ExtendedTerminal with ExtendedProducable with BaseEpsilon {

  def base = Epsilon
}

sealed case class ExtendedEnd(start: ItemSet, end: ItemSet)
  extends ExtendedTerminal with BaseEnd {

  def base = End
}

sealed case class ExtendedNonTerminal(start: ItemSet, name: String, end: ItemSet)
  extends ExtendedProducable with BaseNonTerminal {

  def base = NonTerminal(name)
}

sealed case class ExtendedWord(start: ItemSet, name: String, end: ItemSet)
  extends ExtendedTerminal with ExtendedProducable {

  def base = Word(name)
}

