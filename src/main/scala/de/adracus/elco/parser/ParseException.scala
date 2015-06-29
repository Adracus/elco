package de.adracus.elco.parser

import de.adracus.elco.grammar.Statement
import de.adracus.elco.lexer.core.Token

/**
 * Created by axel on 16/06/15.
 */
sealed trait ParseException extends Exception {
  val expectedStatements: Set[Statement]
  protected def wrap(string: String) = s"'$string'"

  def expected = expectedStatements.map { statement =>
    wrap(statement.name)
  } mkString ", "

  def toString: String
}

case class UnexpectedException(actualToken: Token, expectedStatements: Set[Statement]) extends ParseException {
  override def toString = s"Unexpected $actual at ${actualToken.position}, expected $expected"

  def actual = wrap(actualToken.name)
}
