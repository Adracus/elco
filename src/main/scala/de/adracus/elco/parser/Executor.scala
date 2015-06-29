package de.adracus.elco.parser

import de.adracus.elco.evaluator.Evaluator
import de.adracus.elco.grammar.Grammar
import de.adracus.elco.lexer.core.Lexer

/**
 * Created by axel on 26/06/15.
 */
class Executor(val lexer: Lexer, val grammarBuilder: Grammar, val evaluator: Evaluator) {
  val parser = Parser parsing grammarBuilder.build()

  def evaluate(string: String, printTree: Boolean = false): () => Any = {
    val stream = lexer.lex(string)
    val parseTreeRoot = parser.parse(stream)
    if (printTree)
      println(parseTreeRoot.toFormatted)
    evaluator.eval(parseTreeRoot)
  }
}
