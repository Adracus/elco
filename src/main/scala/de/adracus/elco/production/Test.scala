package de.adracus.elco.production

import de.adracus.elco.base.IntegerInstance
import de.adracus.elco.parser.Parser

/**
 * Created by axel on 07/07/15.
 */
object Test extends App {
  val grammar = ElcoGrammar.build()
  val parser = Parser parsing grammar
  val lexer = ElcoLexer
  val evaluator = new ElcoEvaluator

  val ast = parser.parse(lexer.lex("1 plus 2 plus 3 plus 4"))
  println(ast.toTreeString)

  println(evaluator.evaluate(ast).asInstanceOf[IntegerInstance].value)
}
