package de.adracus.elco.lexer.core

/**
 * Created by axel on 20/05/15.
 */
case class Token (name: String, position: Position, value: Option[Any] = None) {
  def hasValue = value.isDefined
}
