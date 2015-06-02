package de.adracus.elco.grammar.production

import de.adracus.elco.lexer.core.TokenStream
import de.adracus.elco.lexer.production.ElcoLexer
import de.adracus.elco.grammar.core.{Epsilon, Grammar}

/**
 * Created by axel on 26/05/15.
 */
object ElcoGrammar extends Grammar with App {
  'List      := 'Statement & 'List | Epsilon
  'Statement := 'Number & "+" & 'Number
  'Number    := "INTEGER" | "DOUBLE"

  val lexer = new ElcoLexer("1.34 + 20")
  val stream = new TokenStream(lexer)

  parse(stream)
}
