package de.adracus.elco.lexer.core


sealed trait LexException extends Exception {
  val position: Position
}

case class UnrecognizedSymbol(val symbol: String, val position: Position) extends LexException {
  override def toString = s"Unrecognized symbol '$symbol' at $position"
}
