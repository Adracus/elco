package de.adracus.elco.parser

import de.adracus.elco.evaluator.Evaluator
import de.adracus.elco.grammar.GrammarBuilder
import de.adracus.elco.lexer.core.Lexer

/**
 * Created by axel on 26/06/15.
 */
class Executor(val lexer: Lexer, val grammarBuilder: GrammarBuilder, val evaluator: Evaluator) {
  val parser = Parser parsing grammarBuilder.build()

  def evaluate(string: String) = {
    val stream = lexer.lex(string)
    val parseTreeRoot = parser.parse(stream)
    evaluator.evaluate(parseTreeRoot)
  }
}
