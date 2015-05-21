package de.adracus.elco.lexer.core

/**
 * Created by axel on 20/05/15.
 */
case class Position(column: Int, line: Int) {
  override def toString = s"$line:$column"
}
