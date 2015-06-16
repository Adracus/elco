package de.adracus.elco.parser.production

import de.adracus.elco.grammar.production.ElcoGrammarBuilder
import de.adracus.elco.lexer.production.ElcoLexer
import de.adracus.elco.parser.core.Parser

/**
 * Created by axel on 16/06/15.
 */
class ElcoTransformer {
  val parser = Parser parsing ElcoGrammarBuilder.build()
  val lexer = new ElcoLexer()

  def transform(code: String) = {
    val stream = lexer.lex(code)
    val tree = parser.parse(stream)
    println(tree)
  }
}

object ElcoTransformer extends App {
  val transformer = new ElcoTransformer()

  transformer.transform("5 mod 6")
}