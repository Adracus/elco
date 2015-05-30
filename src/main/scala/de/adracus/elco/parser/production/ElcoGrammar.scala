package de.adracus.elco.parser.production

import de.adracus.elco.lexer.core.TokenStream
import de.adracus.elco.lexer.production.ElcoLexer
import de.adracus.elco.parser.core.Grammar

/**
 * Created by axel on 26/05/15.
 */
class ElcoGrammar extends Grammar {
  'If := "if" & "body"
  'List := 'Statement & 'List | 'Statement
  'Test := "if" | "else"
}

object ElcoGrammar extends App {
  val g = new ElcoGrammar

  val stream = new TokenStream(new ElcoLexer("some = 1 + 2"))
  g parse stream
}
