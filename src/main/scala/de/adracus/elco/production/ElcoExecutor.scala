package de.adracus.elco.production

import de.adracus.elco.parser.Executor

/**
 * Created by axel on 26/06/15.
 */
object ElcoExecutor extends Executor(ElcoLexer, ElcoGrammar, new ElcoEvaluator()) with App {
  println(evaluate("if 2 == 2 { 5 } else { 10 }", printTree = true)())
}
