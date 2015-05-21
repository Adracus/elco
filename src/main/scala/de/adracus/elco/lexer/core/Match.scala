package de.adracus.elco.lexer.core

/**
 * Created by axel on 20/05/15.
 */
sealed trait Match {
  def length: Int
}
case class Empty(length: Int) extends Match
case class Hit(name: String, length: Int, value: Option[Any] = None) extends Match
