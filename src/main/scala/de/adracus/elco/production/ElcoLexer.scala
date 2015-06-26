package de.adracus.elco.production

import de.adracus.elco.lexer.consumer.{RegexConsumer, StringConsumer}
import de.adracus.elco.lexer.core.Lexer

/**
 * Created by axel on 21/05/15.
 */
object ElcoLexer extends Lexer {
  val identifier = "[a-zA-Z$_][a-zA-Z0-9$_&|*/%+-]*"

  keyword("if")
  keyword("when")
  keyword("else")
  keyword("while")

  symbol(";")
  symbol("=")
  symbol("+")
  symbol("-")
  symbol("*")
  symbol("^")
  symbol("/")
  symbol("(")
  symbol(")")
  symbol("{")
  symbol("}")

  ignore("\\h")

  addConsumer(new RegexConsumer("\n", "NEWLINE"))
  addConsumer(new RegexConsumer("\\d+", "INTEGER", Some(_.toInt)))
  addConsumer(new RegexConsumer("\\d+\\.\\d+", "DOUBLE", Some(_.toDouble)))
  addConsumer(new RegexConsumer(identifier, "IDENTIFIER", Some(_.toString)))
  addConsumer(new StringConsumer("\"", "\\", newLineSymbol))
}
