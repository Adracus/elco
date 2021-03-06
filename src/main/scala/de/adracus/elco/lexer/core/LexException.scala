package de.adracus.elco.lexer.core


sealed trait LexException extends Exception {
  val position: Position
}

case class UnrecognizedSymbol(symbol: String, position: Position) extends LexException {
  override def toString = s"Unrecognized symbol '$symbol' at $position"
}

case class Unclosed(name: String, position: Position) extends LexException {
  override def toString = s"Unclosed $name at $position"
}