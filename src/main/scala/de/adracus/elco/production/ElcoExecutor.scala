package de.adracus.elco.production

import de.adracus.elco.parser.Executor

/**
 * Created by axel on 26/06/15.
 */
object ElcoExecutor extends Executor(ElcoLexer, ElcoGrammar, new ElcoEvaluator()) with App {
  println(evaluate("a := 3; a = 5", printTree = true)())
}
