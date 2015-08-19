package de.adracus.elco.production

import de.adracus.elco.base.IntegerInstance
import de.adracus.elco.parser.Parser
import de.adracus.elco.production.expressions.{Expression, ElcoFile}

/**
 * Created by axel on 07/07/15.
 */
object Test extends App {
  val grammar = ElcoGrammar.build()
  val parser = Parser parsing grammar
  val lexer = ElcoLexer

  val ast = parser.parse(lexer.lex("b = 10; plus = b.plus; plus(20)"))
  println(ast.toTreeString)

  val file = ElcoFile(ast.asInstanceOf[Expression])

  println(CodeGenerator.generate(file))
}
