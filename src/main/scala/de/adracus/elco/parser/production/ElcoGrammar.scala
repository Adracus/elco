package de.adracus.elco.parser.production

import de.adracus.elco.lexer.core.TokenStream
import de.adracus.elco.lexer.production.ElcoLexer
import de.adracus.elco.parser.core.{Epsilon, Grammar}

/**
 * Created by axel on 26/05/15.
 */
object ElcoGrammar extends Grammar with App {
  'Statement -> 'Number & "+" & 'Number
  'Number    -> "INTEGER" | "DOUBLE"

  val lexer = new ElcoLexer("1 + 2")
  val stream = new TokenStream(lexer)

  parse(stream)
}
