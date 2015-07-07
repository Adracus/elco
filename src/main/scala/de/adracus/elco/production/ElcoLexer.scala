package de.adracus.elco.production

import de.adracus.elco.lexer.consumer.{RegexConsumer, StringConsumer}
import de.adracus.elco.lexer.core.Lexer

/**
 * Created by axel on 21/05/15.
 */
object ElcoLexer extends Lexer {
  val identifierBody = "[a-zA-Z0-9$_&|*/%+-]*"
  val identifierHead = "[a-zA-Z$_]"
  val identifier = s"$identifierHead$identifierBody"

  keyword("if")
  keyword("when")
  keyword("else")
  keyword("while")
  keyword("class")
  keyword("fn")
  keyword("pass")

  symbol(",")
  symbol(":")
  symbol(";")
  symbol(":=")
  symbol("=")

  regex(s"==$identifierBody", "COMPARE_OP")
  regex(s"\\+$identifierBody", "PLUS_OP")
  regex(s"-$identifierBody", "MINUS_OP")
  regex(s"\\*$identifierBody", "MUL_OP")
  regex(s"\\^$identifierBody", "POW_OP")
  regex(s"\\/$identifierBody", "DIV_OP")

  symbol("(")
  symbol(")")
  symbol("{")
  symbol("}")

  ignore("\\h")

  regex("\n", "NEWLINE")
  regex("\\d+", "INTEGER", _.toInt)
  regex("\\d+\\.\\d+", "DOUBLE", _.toDouble)
  regex(identifier, "IDENTIFIER", identity[String])

  addConsumer(new StringConsumer("\"", "\\", newLineSymbol))
}
