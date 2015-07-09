package de.adracus.elco.production

import de.adracus.elco.parser.Parser

/**
 * Created by axel on 07/07/15.
 */
object Test extends App {
  val grammar = ElcoGrammar.build()
  println(ElcoGrammar.toString)
  val parser = Parser parsing grammar
  val lexer = ElcoLexer

  val ast = parser.parse(lexer.lex("10"))
}
