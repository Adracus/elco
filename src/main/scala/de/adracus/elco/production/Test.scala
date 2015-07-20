package de.adracus.elco.production

import de.adracus.elco.parser.Parser

/**
 * Created by axel on 07/07/15.
 */
object Test extends App {
  val grammar = ElcoGrammar.build()
  val parser = Parser parsing grammar
  val lexer = ElcoLexer
  val evaluator = new ElcoEvaluator

  val ast = parser.parse(lexer.lex("fn bla(a, b) { d := a; c := b}; bla(1, 2)"))
  println(ast.toTreeString)

  println(evaluator.evaluate(ast))
}
